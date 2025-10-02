package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.Company;
import boev.app.models.units.MilitaryUnit;
import boev.app.models.units.Platoon;
import boev.app.models.units.Squad;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.platoon.PlatoonDto;
import boev.app.payload.units.platoon.PlatoonResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.CompanyRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.repository.units.PlatoonRepository;
import boev.app.repository.units.SquadRepository;
import boev.app.services.interfaces.units.PlatoonService;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlatoonServiceImpl implements PlatoonService {
    private final PlatoonRepository platoonRepository;
    private final CompanyRepository companyRepository;
    private final SquadRepository squadRepository;
    private final SolderRepository solderRepository;
    private final ModelMapper modelMapper;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return platoonRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<PlatoonResponse> getAll() {
        return platoonRepository.findAll().stream().map(modelMapper::toPlatoonResponse).collect(Collectors.toList());
    }

    @Transactional
    public PlatoonResponse get(long id) {
        return modelMapper.toPlatoonResponse(platoonRepository.findById(id).orElseThrow(() -> new Error404("Platoon not found")));
    }

    @Transactional
    public PlatoonResponse delete(long id) {
        Platoon platoon = platoonRepository.findById(id).orElseThrow(() -> new Error404("Platoon not found"));
        if(platoon.getSquads() != null){
            for(Squad squad : platoon.getSquads()){
                squad.setPlatoon(null);
                try{
                    squadRepository.save(squad);
                }  catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(platoon.getCommander()!= null){
            Soldier soldier = platoon.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(platoon.getCompany() != null){
            Company company = platoon.getCompany();
            company.getPlatoons().removeIf(s -> s.getId().equals(id));
            try{
                companyRepository.save(company);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        PlatoonResponse platoonResponse = modelMapper.toPlatoonResponse(platoon);
        platoonRepository.deleteById(id);
        return platoonResponse;
    }

    @Transactional
    public PlatoonResponse create(PlatoonDto platoonDto) {
        Platoon platoon = modelMapper.toPlatoon(platoonDto);
        platoon.setSquads(new ArrayList<>());

        Company company = null;

        Soldier soldier = null;
        if(platoonDto.getCommanderId() != null){
            soldier = solderRepository.findById(platoonDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        platoon.setCommander(soldier);

        if(platoonDto.getCompanyId() != null){
            company = companyRepository.findById(platoonDto.getCompanyId()).orElseThrow(() -> new Error404("Company not found"));
        }

        platoon.setCompany(company);

        if(platoonDto.getSquadsId() != null){
            for(Long ids : platoonDto.getSquadsId()){
                Squad squad = squadRepository.findById(ids).orElseThrow(() -> new Error404("Platoon not found"));
                squad.setPlatoon(platoon);
                platoon.getSquads().add(squad);

                try{
                    squadRepository.save(squad);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            platoon = platoonRepository.saveAndFlush(platoon);
            if(soldier != null){
                soldier.setCommandedUnit(platoon);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toPlatoonResponse(platoon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public PlatoonResponse update(PlatoonDto platoonDto, long id) {
        Platoon platoon = platoonRepository.findById(id).orElseThrow(() -> new Error404("Platoon not found"));

        for(Squad squads : platoon.getSquads()){
            squads.setPlatoon(null);
            try{
                squadRepository.saveAndFlush(squads);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        platoon.setSquads(new ArrayList<>());

        Soldier soldier = null;
        if(platoonDto.getCommanderId() != null){
            soldier = solderRepository.findById(platoonDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        platoon.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(platoon);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        Company company = null;
        if(platoonDto.getCompanyId() != null){
            company = companyRepository.findById(platoonDto.getCompanyId()).orElseThrow(() -> new Error404("Company not found"));
        }

        platoon.setCompany(company);


        if(platoonDto.getSquadsId() != null){
            for(Long ids : platoonDto.getSquadsId()){
                System.out.println(ids);
                Squad squad = squadRepository.findById(ids).orElseThrow(() -> new Error404("Platoon not found"));
                squad.setPlatoon(platoon);
                platoon.getSquads().add(squad);

                try{
                    squadRepository.save(squad);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        platoon.setName(platoonDto.getName());

        try{
            platoon = platoonRepository.save(platoon);
            return modelMapper.toPlatoonResponse(platoon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
