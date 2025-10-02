package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.Army;
import boev.app.models.units.Corp;
import boev.app.models.units.Division;
import boev.app.models.units.MilitaryUnit;
import boev.app.payload.units.corp.CorpDto;
import boev.app.payload.units.corp.CorpResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.ArmyRepository;
import boev.app.repository.units.CorpsRepository;
import boev.app.repository.units.DivisionRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.services.interfaces.units.CorpService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CorpServiceImpl implements CorpService {
    private final ModelMapper modelMapper;
    private final CorpsRepository corpsRepository;
    private final SolderRepository solderRepository;
    private final DivisionRepository divisionRepository;
    private final ArmyRepository armyRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return corpsRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<CorpResponse> getAll() {
        return corpsRepository.findAll().stream().map(modelMapper::toCorpResponse).collect(Collectors.toList());
    }

    @Transactional
    public CorpResponse get(long id) {
        return modelMapper.toCorpResponse(corpsRepository.findById(id).orElseThrow(() -> new Error404("Corp not found")));
    }

    @Transactional
    public CorpResponse delete(long id) {
        Corp corp = corpsRepository.findById(id).orElseThrow(() -> new Error404("Corp not found"));
        if(corp.getDivisions() != null){
            for(Division division : corp.getDivisions()){
                division.setCorp(null);
                try{
                    divisionRepository.save(division);
                } catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(corp.getCommander()!= null){
            Soldier soldier = corp.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(corp.getArmy() != null){
            Army army = corp.getArmy();
            army.getCorps().removeIf(s -> s.getId().equals(id));

            try{
                armyRepository.save(army);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        CorpResponse corpResponse = modelMapper.toCorpResponse(corp);
        corpsRepository.deleteById(id);
        return corpResponse;
    }

    @Transactional
    public CorpResponse update(CorpDto corpDto, long id) {
        Corp corp = corpsRepository.findById(id).orElseThrow(() -> new Error404("Corp not found"));

        for(Division division : corp.getDivisions()){
            division.setCorp(null);
            try{
                divisionRepository.saveAndFlush(division);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        corp.setDivisions(new ArrayList<>());

        Soldier soldier = null;
        if(corpDto.getCommanderId() != null){
            soldier = solderRepository.findById(corpDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
            if(soldier.getCommandedUnit() != null && !soldier.getCommandedUnit().getId().equals(id)){
                MilitaryUnit militaryUnit = soldier.getCommandedUnit();
                militaryUnit.setCommander(null);
                soldier.setCommandedUnit(null);
                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        corp.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(corp);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        Army army = null;
        if(corpDto.getArmyId() != null){
            army = armyRepository.findById(corpDto.getArmyId()).orElseThrow(() -> new Error404("Army not found"));
        }
        corp.setArmy(army);

        if(corpDto.getDivisionsId() != null){
            for(Long ids : corpDto.getDivisionsId()){
                Division division = divisionRepository.findById(ids).orElseThrow(() -> new Error404("division not found"));
                corp.getDivisions().add(division);
                division.setCorp(corp);

                try{
                    divisionRepository.save(division);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        corp.setName(corpDto.getName());

        try{
            corp = corpsRepository.save(corp);
            return modelMapper.toCorpResponse(corp);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public CorpResponse create(CorpDto corpDto) {
        Corp corp = modelMapper.toCorp(corpDto);
        corp.setDivisions(new ArrayList<>());


        Soldier soldier = null;
        if(corpDto.getCommanderId() != null){
            soldier = solderRepository.findById(corpDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
            if(soldier.getCommandedUnit() != null){
                MilitaryUnit militaryUnit = soldier.getCommandedUnit();
                militaryUnit.setCommander(null);
                soldier.setCommandedUnit(null);
                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        corp.setCommander(soldier);

        Army army = null;
        if(corpDto.getArmyId() != null){
            army = armyRepository.findById(corpDto.getArmyId()).orElseThrow(() -> new Error404("Army not found"));
        }
        corp.setArmy(army);

        if(corpDto.getDivisionsId() != null){
            for(Long ids : corpDto.getDivisionsId()){
                Division division = divisionRepository.findById(ids).orElseThrow(() -> new Error404("division not found"));
                corp.getDivisions().add(division);
                division.setCorp(corp);

                try{
                    divisionRepository.save(division);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            corp = corpsRepository.saveAndFlush(corp);
            if(soldier != null){
                soldier.setCommandedUnit(corp);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toCorpResponse(corp);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
