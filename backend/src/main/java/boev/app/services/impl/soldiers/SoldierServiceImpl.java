package boev.app.services.impl.soldiers;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Rank;
import boev.app.models.soldiers.Soldier;
import boev.app.models.soldiers.records.OfficerRecord;
import boev.app.models.soldiers.records.PrivateRecord;
import boev.app.models.soldiers.records.SergeantRecord;
import boev.app.models.soldiers.specialty.MilitarySpecialtyEntity;
import boev.app.models.units.MilitaryUnit;
import boev.app.models.units.Squad;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.payload.soldiers.records.officer.OfficerRecordDto;
import boev.app.payload.soldiers.records.privates.PrivateRecordDto;
import boev.app.payload.soldiers.records.sergeant.SergeantRecordDto;
import boev.app.payload.soldiers.solider.SoldierDto;
import boev.app.payload.soldiers.solider.SoldierResponse;
import boev.app.payload.soldiers.solider.SoldierSimpleDto;
import boev.app.repository.soliders.MilitarySpeciallyEntityRepository;
import boev.app.repository.soliders.RankRepository;

import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.repository.units.SquadRepository;
import boev.app.services.interfaces.soldiers.SoldierService;
import boev.app.util.SwitchName;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SoldierServiceImpl implements SoldierService {
    private final RankRepository rankRepository;
    private final SolderRepository solderRepository;
    private final SquadRepository squadRepository;
    private final MilitaryUnitRepository militaryUnitRepository;
    private final ModelMapper modelMapper;
    private final MilitarySpeciallyEntityRepository speciallyEntityRepository;

    @Transactional
    public SoldierSimpleDto getSimple(long id) {
        Soldier soldier = solderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Soldier not found: " + id));
        return mapToSimpleDto(soldier);
    }

    @Transactional
    public List<SoldierSimpleDto> getAll() {
        return solderRepository.findAll().stream().map(this::mapToSimpleDto).collect(Collectors.toList());
    }

    @Transactional
    public SoldierResponse get(long id) {
        return modelMapper.toSoldierResponse(solderRepository.findById(id).orElseThrow(() -> new Error404("Soldier not found")));
    }

    @Transactional
    public SoldierResponse delete(long id) {
        Soldier soldier = solderRepository.findById(id).orElseThrow(() -> new Error404("Soldier not found"));
        if(soldier.getSquad() != null){
            Squad squad = soldier.getSquad();
            squad.getSoldiers().removeIf(s -> s.getId().equals(id));
            try{
                squadRepository.save(squad);
            } catch (DataIntegrityViolationException e) {
                throw new Error404("Squad not found");
            }
        }
        if(soldier.getCommandedUnit() != null){
            MilitaryUnit militaryUnit = soldier.getCommandedUnit();
            militaryUnit.setCommander(null);
            try{
                militaryUnitRepository.save(militaryUnit);
            } catch (DataIntegrityViolationException e) {
                throw new Error404("MilitaryUnit not found");
            }
        }

        SoldierResponse soldierResponse = modelMapper.toSoldierResponse(soldier);
        solderRepository.deleteById(id);
        return soldierResponse;
    }

    @Transactional
    public SoldierResponse update(long id, SoldierDto soldierDto) {
        Soldier soldier = solderRepository.findById(id).orElseThrow(() -> new Error404("Soldier not found"));

        Rank rank = rankRepository.findById(soldierDto.getRankId()).orElseThrow(() -> new Error404("Rank not found"));
        soldier.setRank(rank);

        Squad squad = null;

        if(soldier.getCommandedUnit() != null){
            MilitaryUnit militaryUnit = soldier.getCommandedUnit();
            militaryUnit.setCommander(null);

            try{
                militaryUnitRepository.saveAndFlush(militaryUnit);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(soldierDto.getSquadId() != null){
            squad = squadRepository.findById(soldierDto.getSquadId()).orElseThrow(() -> new Error404("Squad not found"));
        }
        soldier.setSquad(squad);

        MilitaryUnit militaryUnit = null;
        if(soldierDto.getCommandedUnitId() != null){
            militaryUnit = militaryUnitRepository.findById(soldierDto.getCommandedUnitId()).orElseThrow(() -> new Error404("MilitaryUnit not found"));
            if(militaryUnit.getCommander() != null){
                Soldier soldier1 = militaryUnit.getCommander();
                soldier1.setCommandedUnit(null);
                militaryUnit.setCommander(null);
                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier1);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        soldier.setCommandedUnit(militaryUnit);
        if(militaryUnit != null){
            militaryUnit.setCommander(soldier);
            try{
                militaryUnitRepository.saveAndFlush(militaryUnit);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if (soldier.getPrivateRecords() != null) {
            soldier.getPrivateRecords().clear();
        }
        if (soldier.getSergeantRecords() != null) {
            soldier.getSergeantRecords().clear();
        }
        if (soldier.getOfficerRecords() != null) {
            soldier.getOfficerRecords().clear();
        }

        if(soldier.getSpecialties() != null){
            soldier.getSpecialties().clear();
        }

        for(PrivateRecordDto privateRecordDto : soldierDto.getPrivateRecords()){
            PrivateRecord privateRecord = modelMapper.toPrivateRecord(privateRecordDto);
            privateRecord.setSoldier(soldier);
            privateRecord.setRank(rankRepository.findById(privateRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getPrivateRecords().add(privateRecord);
        }

        for(SergeantRecordDto sergeantRecordDto : soldierDto.getSergeantRecords()){
            SergeantRecord sergeantRecord = modelMapper.toSergeantRecord(sergeantRecordDto);
            sergeantRecord.setSoldier(soldier);
            sergeantRecord.setRank(rankRepository.findById(sergeantRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getSergeantRecords().add(sergeantRecord);
        }

        for(OfficerRecordDto officerRecordDto : soldierDto.getOfficerRecords()){
            OfficerRecord officerRecord = modelMapper.toOfficerRecord(officerRecordDto);
            officerRecord.setSoldier(soldier);
            officerRecord.setRank(rankRepository.findById(officerRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getOfficerRecords().add(officerRecord);
        }

        if(soldierDto.getSpecialtiesId() != null){
            for(Long ids : soldierDto.getSpecialtiesId()){
                MilitarySpecialtyEntity militarySpecialtyEntity = speciallyEntityRepository.findById(ids).orElseThrow(() -> new Error404("Spealty not found"));
                soldier.getSpecialties().add(militarySpecialtyEntity);
            }
        }

        soldier.setBirthDay(soldierDto.getBirthDay());
        soldier.setFirstName(soldierDto.getFirstName());
        soldier.setSecondName(soldierDto.getSecondName());
        soldier.setFatherName(soldierDto.getFatherName());

        try{
            soldier = solderRepository.save(soldier);
            return modelMapper.toSoldierResponse(soldier);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public SoldierResponse create(SoldierDto soldierDto) {
//        Soldier soldier = modelMapper.toSoldier(soldierDto);
        Soldier soldier = new Soldier();
        soldier.setFirstName(soldierDto.getFirstName());
        soldier.setFatherName(soldierDto.getFatherName());
        soldier.setSecondName(soldierDto.getSecondName());
        soldier.setBirthDay(soldierDto.getBirthDay());
        soldier.setOfficerRecords(new ArrayList<>());
        soldier.setPrivateRecords(new ArrayList<>());
        soldier.setSergeantRecords(new ArrayList<>());
        soldier.setSpecialties(new HashSet<>());

        Rank rank = rankRepository.findById(soldierDto.getRankId()).orElseThrow(() -> new Error404("Rank not found"));
        soldier.setRank(rank);


        MilitaryUnit militaryUnit = null;
        if(soldierDto.getCommandedUnitId() != null){
            militaryUnit = militaryUnitRepository.findById(soldierDto.getCommandedUnitId()).orElseThrow(() -> new Error404("MilitaryUnit not found"));
            if(militaryUnit.getCommander() != null){
                Soldier soldier1 =  militaryUnit.getCommander();
                soldier1.setCommandedUnit(null);
                militaryUnit.setCommander(null);

                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier1);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        soldier.setCommandedUnit(militaryUnit);

        Squad squad = null;
        if(soldierDto.getSquadId() != null){
            squad = squadRepository.findById(soldierDto.getSquadId()).orElseThrow(() -> new Error404("Squad not found"));
        }
        soldier.setSquad(squad);

        if(soldierDto.getSpecialtiesId() != null){
            for(Long ids : soldierDto.getSpecialtiesId()){
                MilitarySpecialtyEntity militarySpecialtyEntity = speciallyEntityRepository.findById(ids).orElseThrow(() -> new Error404("Spealty not found"));
                soldier.getSpecialties().add(militarySpecialtyEntity);
            }
        }

        for(PrivateRecordDto privateRecordDto : soldierDto.getPrivateRecords()){
            PrivateRecord privateRecord = modelMapper.toPrivateRecord(privateRecordDto);
            privateRecord.setSoldier(soldier);
            privateRecord.setRank(rankRepository.findById(privateRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getPrivateRecords().add(privateRecord);
        }

        for(SergeantRecordDto sergeantRecordDto : soldierDto.getSergeantRecords()){
            SergeantRecord sergeantRecord = modelMapper.toSergeantRecord(sergeantRecordDto);
            sergeantRecord.setSoldier(soldier);
            sergeantRecord.setRank(rankRepository.findById(sergeantRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getSergeantRecords().add(sergeantRecord);
        }

        for(OfficerRecordDto officerRecordDto : soldierDto.getOfficerRecords()){
            OfficerRecord officerRecord = modelMapper.toOfficerRecord(officerRecordDto);
            officerRecord.setSoldier(soldier);
            officerRecord.setRank(rankRepository.findById(officerRecordDto.getRankId()).orElseThrow(() -> new Error404("Rank not found")));
            soldier.getOfficerRecords().add(officerRecord);
        }

        try{
            soldier = solderRepository.saveAndFlush(soldier);
            if(militaryUnit != null){
                militaryUnit.setCommander(soldier);
                militaryUnitRepository.saveAndFlush(militaryUnit);
            }
            return modelMapper.toSoldierResponse(soldier);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    private  SoldierSimpleDto mapToSimpleDto(Soldier soldier) {
        return new SoldierSimpleDto(
                soldier.getId(),
                soldier.getFirstName(),
                soldier.getSecondName(),
                soldier.getFatherName(),
                soldier.getBirthDay(),
                soldier.getSpecialties() != null ?
                        soldier.getSpecialties().stream()
                                .map(specialty -> specialty.getName().name())
                                .collect(Collectors.toList()) :
                        Collections.emptyList(),
                soldier.getSpecialties() != null ?
                        soldier.getSpecialties().stream()
                                .map(specialty -> specialty.getId())
                                .collect(Collectors.toList()) :
                        Collections.emptyList(),
                soldier.getSquad() != null ? soldier.getSquad().getName() : null,
                soldier.getSquad() != null ? soldier.getSquad().getId() : null,
                soldier.getCommandedUnit() != null ? soldier.getCommandedUnit().getName() : null,
                soldier.getCommandedUnit() != null ? soldier.getCommandedUnit().getId() : null,
                soldier.getRank() != null ? SwitchName.translateRussianArmyRank(soldier.getRank().getName()): null,
                soldier.getRank() != null ? soldier.getRank().getId(): null,
                soldier.getPrivateRecords() != null ? soldier.getPrivateRecords().stream().map(modelMapper::toPrivateRecordResponse).collect(Collectors.toList()) : Collections.emptyList(),
                soldier.getSergeantRecords() != null ? soldier.getSergeantRecords().stream().map(modelMapper::toSergeantRecordResponse).collect(Collectors.toList())  : Collections.emptyList(),
                soldier.getOfficerRecords() != null ? soldier.getOfficerRecords().stream().map(modelMapper::toOfficerRecordResponse).collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}
