package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.*;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.payload.soldiers.solider.SoldierMinimumDto;
import boev.app.payload.units.division.DivisionDto;
import boev.app.payload.units.division.DivisionResponse;
import boev.app.payload.units.militaryFormation.EquipmentAll;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.CorpsRepository;
import boev.app.repository.units.DivisionRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.services.interfaces.units.DivisionService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DivisionServiceImpl implements DivisionService {
    private final DivisionRepository divisionRepository;
    private final ModelMapper modelMapper;
    private final MilitaryFormationRepository militaryFormationRepository;
    private final CorpsRepository corpsRepository;
    private final SolderRepository solderRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return divisionRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<DivisionResponse> getAll() {
        return divisionRepository.findAll().stream().map(modelMapper::toDivisionResponse).collect(Collectors.toList());
    }

    @Transactional
    public DivisionResponse get(long id) {
        return modelMapper.toDivisionResponse(divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found")));
    }

    @Transactional
    public DivisionResponse delete(long id) {
        Division division = divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found"));
        if(division.getFormations() != null){
            for(MilitaryFormation formation : division.getFormations()){
                formation.setDivision(null);

                try{
                    militaryFormationRepository.save(formation);
                } catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        if(division.getCommander()!= null){
            Soldier soldier = division.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        if(division.getCorp() != null){
            Corp corp = division.getCorp();
            corp.getDivisions().removeIf(s -> s.getId().equals(id));

            try{
                corpsRepository.save(corp);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        DivisionResponse divisionResponse = modelMapper.toDivisionResponse(division);
        divisionRepository.deleteById(id);
        return divisionResponse;
    }

    @Transactional
    public DivisionResponse create(DivisionDto divisionDto) {
        Division division = modelMapper.toDivision(divisionDto);
        division.setFormations(new ArrayList<>());

        Soldier soldier = null;
        if(divisionDto.getCommanderId() != null){
            soldier = solderRepository.findById(divisionDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        division.setCommander(soldier);

        Corp corp = null;
        if(divisionDto.getCorpId() != null){
            corp = corpsRepository.findById(divisionDto.getCorpId()).orElseThrow(() -> new Error404("Corp not found"));
        }
        division.setCorp(corp);

        if(divisionDto.getFormationsId() != null){
            for(Long ids : divisionDto.getFormationsId()){
                MilitaryFormation militaryFormation = militaryFormationRepository.findById(ids).orElseThrow(() -> new Error404("Formation not found"));
                division.getFormations().add(militaryFormation);
                militaryFormation.setDivision(division);
                try{
                    militaryFormationRepository.save(militaryFormation);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            division = divisionRepository.saveAndFlush(division);
            if(soldier != null){
                soldier.setCommandedUnit(division);
                solderRepository.saveAndFlush(soldier);
            }

            return modelMapper.toDivisionResponse(division);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public DivisionResponse update(DivisionDto divisionDto, long id) {
        Division division = divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found"));

        for(MilitaryFormation militaryFormation : division.getFormations()){
            militaryFormation.setDivision(null);
            try{
                militaryFormationRepository.saveAndFlush(militaryFormation);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        division.setFormations(new ArrayList<>());


        Soldier soldier = null;
        if(divisionDto.getCommanderId() != null){
            soldier = solderRepository.findById(divisionDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        division.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(division);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        Corp corp = null;
        if(divisionDto.getCorpId() != null){
            corp = corpsRepository.findById(divisionDto.getCorpId()).orElseThrow(() -> new Error404("Corp not found"));
        }
        division.setCorp(corp);

        if(divisionDto.getFormationsId() != null){
            for(Long ids : divisionDto.getFormationsId()){
                MilitaryFormation militaryFormation = militaryFormationRepository.findById(ids).orElseThrow(() -> new Error404("Formation not found"));
                division.getFormations().add(militaryFormation);
                militaryFormation.setDivision(division);
                try{
                    militaryFormationRepository.save(militaryFormation);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        division.setName(divisionDto.getName());


        try{
            division = divisionRepository.save(division);
            return modelMapper.toDivisionResponse(division);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Override
    public List<MilitaryUnitResponse> getMilitaryFormation(long id) {
        return divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found")).getFormations().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

//    @Override
//    public List<SoldierMinimumDto> getOfficer(long id) {
//        Division division = divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found"));
//
//    }

    @Override
    public List<SoldierMinimumDto> getNotOfficer(long id) {
        return List.of();
    }

    @Override
    public List<HeadquarterResponse> getHeadquarter(long id) {
        return List.of();
    }

    @Override
    public EquipmentAll getEquipment(long id) {
        return null;
    }

    @Override
    public List<MilitarySpecialtyResponse> getSpecialty(long id) {
        return List.of();
    }

    @Override
    public DivisionResponse MoreMilitaryFormation() {
        return null;
    }

    @Override
    public List<SoldierMinimumDto> getOfficer(long id) {

        Division division = divisionRepository.findById(id).orElseThrow(() -> new Error404("Division not found"));
        List<Soldier> officers = new ArrayList<>();

        // Проверяем командира дивизии
        if (division.getCommander() != null &&
                division.getCommander().getRank() != null &&
                division.getCommander().getRank().getCategory() == RankCategory.OFFICER) {
            officers.add(division.getCommander());
        }

        // Обходим все формирования (MilitaryFormation) в дивизии
        if (division.getFormations() != null) {
            for (MilitaryFormation formation : division.getFormations()) {
                collectOfficersFromFormation(formation, officers);
            }
        }

        return officers.stream().map(modelMapper::toSoldierMinimumDto).collect(Collectors.toList());
    }

    private void collectOfficersFromFormation(MilitaryFormation formation, List<Soldier> officers) {
        if (formation == null) {
            return;
        }

        // Проверяем командира формирования
        if (formation.getCommander() != null &&
                formation.getCommander().getRank() != null &&
                formation.getCommander().getRank().getCategory() == RankCategory.OFFICER) {
            officers.add(formation.getCommander());
        }

        // Обходим все роты (Company) в формировании
        if (formation.getCompanies() != null) {
            for (Company company : formation.getCompanies()) {
                collectOfficersFromCompany(company, officers);
            }
        }
    }

    private void collectOfficersFromCompany(Company company, List<Soldier> officers) {
        if (company == null) {
            return;
        }

        // Проверяем командира роты
        if (company.getCommander() != null &&
                company.getCommander().getRank() != null &&
                company.getCommander().getRank().getCategory() == RankCategory.OFFICER) {
            officers.add(company.getCommander());
        }

        // Обходим все взводы (Platoon) в роте
        if (company.getPlatoons() != null) {
            for (Platoon platoon : company.getPlatoons()) {
                collectOfficersFromPlatoon(platoon, officers);
            }
        }
    }

    private void collectOfficersFromPlatoon(Platoon platoon, List<Soldier> officers) {
        if (platoon == null) {
            return;
        }

        // Проверяем командира взвода
        if (platoon.getCommander() != null &&
                platoon.getCommander().getRank() != null &&
                platoon.getCommander().getRank().getCategory() == RankCategory.OFFICER) {
            officers.add(platoon.getCommander());
        }

        // Обходим все отделения (Squad) во взводе
        if (platoon.getSquads() != null) {
            for (Squad squad : platoon.getSquads()) {
                collectOfficersFromSquad(squad, officers);
            }
        }
    }

    private void collectOfficersFromSquad(Squad squad, List<Soldier> officers) {
        if (squad == null) {
            return;
        }

        // Проверяем командира отделения
        if (squad.getCommander() != null &&
                squad.getCommander().getRank() != null &&
                squad.getCommander().getRank().getCategory() == RankCategory.OFFICER) {
            officers.add(squad.getCommander());
        }

        // Проверяем всех солдат в отделении
        if (squad.getSoldiers() != null) {
            for (Soldier soldier : squad.getSoldiers()) {
                if (soldier != null &&
                        soldier.getRank() != null &&
                        soldier.getRank().getCategory() == RankCategory.OFFICER) {
                    officers.add(soldier);
                }
            }
        }
    }
}
