package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.MilitaryUnit;
import boev.app.models.units.Platoon;
import boev.app.models.units.Squad;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.squad.SquadDto;
import boev.app.payload.units.squad.SquadResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.repository.units.PlatoonRepository;
import boev.app.repository.units.SquadRepository;
import boev.app.services.interfaces.units.SquadService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SquadServiceImpl implements SquadService {
    private final SquadRepository squadRepository;
    private final SolderRepository solderRepository;
    private final PlatoonRepository platoonRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return squadRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<SquadResponse> getAll() {
        return squadRepository.findAll().stream().map(modelMapper::toSquadResponse).collect(Collectors.toList());
    }
    @Transactional
    public SquadResponse get(long id) {
        return modelMapper.toSquadResponse(squadRepository.findById(id).orElseThrow(() -> new Error404("Squad not found")));
    }

    @Transactional
    public SquadResponse delete(long id) {
        Squad squad = squadRepository.findById(id).orElseThrow(() -> new Error404("Squad not found"));

        if(squad.getSoldiers() != null){
            for(Soldier soldier : squad.getSoldiers()){
                soldier.setSquad(null);
                try{
                    solderRepository.save(soldier);
                } catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        if(squad.getCommander()!= null){
            Soldier soldier = squad.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(squad.getPlatoon() != null){
            Platoon platoon = squad.getPlatoon();
            platoon.getSquads().removeIf(s -> s.getId().equals(id));
            try{
                platoonRepository.save(platoon);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        SquadResponse squadResponse = modelMapper.toSquadResponse(squad);
        squadRepository.deleteById(id);
        return squadResponse;
    }

    @Transactional
    public SquadResponse create(SquadDto squadDto) {
        Squad squad = modelMapper.toSquad(squadDto);
        squad.setSoldiers(new ArrayList<>());

        Soldier soldier = null;
        if(squadDto.getCommanderId() != null){
            soldier = solderRepository.findById(squadDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        squad.setCommander(soldier);
//        if(soldier != null){
//            soldier.setCommandedUnit(squad);
//            try{
//                solderRepository.saveAndFlush(soldier);
//            }catch (DataIntegrityViolationException e) {
//                throw new Error500();
//            }
//        }

        Platoon platoon = null;
        if(squadDto.getPlatoonId() != null){
            platoon = platoonRepository.findById(squadDto.getPlatoonId()).orElseThrow(() -> new Error404("Platoon not found"));
        }
        squad.setPlatoon(platoon);

        if(squadDto.getSoldiersId() != null){
            for(Long ids : squadDto.getSoldiersId()){
                Soldier soldier1 = solderRepository.findById(ids).orElseThrow(() -> new Error404("Soldier not found"));
                soldier1.setSquad(squad);
                squad.getSoldiers().add(soldier1);

                try{
                    solderRepository.save(soldier1);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            squad = squadRepository.saveAndFlush(squad);
            if(soldier != null){
                soldier.setCommandedUnit(squad);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toSquadResponse(squad);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public SquadResponse update(SquadDto squadDto, long id) {
        Squad squad = squadRepository.findById(id).orElseThrow(() -> new Error404("Squad not found"));

        for (Soldier soldier1 : squad.getSoldiers()) {// Удаляем связь с текущим взводом
            soldier1.setSquad(null);
            try {
                solderRepository.save(soldier1);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        squad.setSoldiers(new ArrayList<>());

        Soldier soldier = null;
        if(squadDto.getCommanderId() != null){
            soldier = solderRepository.findById(squadDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        squad.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(squad);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        Platoon platoon = null;
        if(squadDto.getPlatoonId() != null){
            platoon = platoonRepository.findById(squadDto.getPlatoonId()).orElseThrow(() -> new Error404("Platoon not found"));
        }
        squad.setPlatoon(platoon);

        if(squadDto.getSoldiersId() != null){
            for(Long ids : squadDto.getSoldiersId()){
                Soldier soldier1 = solderRepository.findById(ids).orElseThrow(() -> new Error404("Soldier not found"));
                soldier1.setSquad(squad);
                squad.getSoldiers().add(soldier1);

                try{
                    solderRepository.save(soldier1);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }


        squad.setName(squadDto.getName());

        try{
            squad = squadRepository.save(squad);
            return modelMapper.toSquadResponse(squad);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
