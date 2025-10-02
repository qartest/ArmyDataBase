package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.Army;
import boev.app.models.units.Corp;
import boev.app.models.units.MilitaryUnit;
import boev.app.payload.units.army.ArmyDto;
import boev.app.payload.units.army.ArmyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.ArmyRepository;
import boev.app.repository.units.CorpsRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.services.interfaces.units.ArmyService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArmyServiceImpl implements ArmyService {
    private final ArmyRepository armyRepository;
    private final ModelMapper modelMapper;
    private final SolderRepository solderRepository;
    private final CorpsRepository corpsRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Override
    public List<MilitaryUnitResponse> getAllSimple() {
        return armyRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Override
    public List<ArmyResponse> getAll() {
        return armyRepository.findAll().stream().map(modelMapper::toArmyResponse).collect(Collectors.toList());
    }

    @Transactional
    public ArmyResponse get(long id) {
        return modelMapper.toArmyResponse( armyRepository.findById(id).orElseThrow(() -> new Error404("Army not found")));
    }

    @Transactional
    public ArmyResponse delete(long id) {
        Army army = armyRepository.findById(id).orElseThrow(() -> new Error404("Army not found"));
        if(army.getCorps() != null){
            for(Corp corp : army.getCorps()){
                corp.setArmy(null);
                try{
                    corpsRepository.save(corp);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(army.getCommander()!= null){
            Soldier soldier = army.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        ArmyResponse armyResponse = modelMapper.toArmyResponse(army);
        armyRepository.delete(army);
        return armyResponse;
    }

    @Transactional
    public ArmyResponse create(ArmyDto armyDto) {
        Army army = modelMapper.toArmy(armyDto);
        army.setCorps(new ArrayList<>());


        Soldier soldier = null;
        if(armyDto.getCommanderId() != null){
            soldier = solderRepository.findById(armyDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        army.setCommander(soldier);

        if(armyDto.getCorpsId() != null){
            for(Long ids : armyDto.getCorpsId()){
                Corp corp = corpsRepository.findById(ids).orElseThrow(() -> new Error404("Corp not found"));
                army.getCorps().add(corp);
                corp.setArmy(army);

                try{
                    corpsRepository.save(corp);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            army = armyRepository.saveAndFlush(army);
            if(soldier != null){
                soldier.setCommandedUnit(army);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toArmyResponse(army);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public ArmyResponse update(ArmyDto armyDto, long id) {
        Army army = armyRepository.findById(id).orElseThrow(() -> new Error404("Army not found"));

        for(Corp corp : army.getCorps()){
            corp.setArmy(null);
            try{
                corpsRepository.saveAndFlush(corp);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        army.setCorps(new ArrayList<>());

        Soldier soldier = null;
        if(armyDto.getCommanderId() != null){
            soldier = solderRepository.findById(armyDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        army.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(army);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        if(armyDto.getCorpsId() != null){
            for(Long ids : armyDto.getCorpsId()){
                Corp corp = corpsRepository.findById(ids).orElseThrow(() -> new Error404("Corp not found"));
                army.getCorps().add(corp);
                corp.setArmy(army);

                try{
                    corpsRepository.save(corp);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        army.setName(armyDto.getName());
        try{
            army = armyRepository.save(army);
            return modelMapper.toArmyResponse(army);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
