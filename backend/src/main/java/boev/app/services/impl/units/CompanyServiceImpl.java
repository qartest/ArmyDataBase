package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.*;
import boev.app.payload.units.company.CompanyDto;
import boev.app.payload.units.company.CompanyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.CompanyRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.repository.units.PlatoonRepository;
import boev.app.services.interfaces.units.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final MilitaryFormationRepository militaryFormationRepository;
    private final SolderRepository solderRepository;
    private final PlatoonRepository platoonRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return companyRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<CompanyResponse> getAll() {
        return  companyRepository.findAll().stream().map(modelMapper::toCompanyResponse).collect(Collectors.toList());
    }

    @Transactional
    public CompanyResponse get(long id) {
        return modelMapper.toCompanyResponse(companyRepository.findById(id).orElseThrow(() -> new Error404("Company not found")));
    }

    @Transactional
    public CompanyResponse delete(long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new Error404("Company not found"));

        if(company.getPlatoons() != null){
            for(Platoon platoon : company.getPlatoons()){
                platoon.setCompany(null);
                try{
                    platoonRepository.save(platoon);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        if(company.getCommander()!= null){
            Soldier soldier = company.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(company.getFormation() != null){
            MilitaryFormation militaryFormation = company.getFormation();
            militaryFormation.getCompanies().removeIf(s -> s.getId().equals(id));

            try{
                militaryFormationRepository.save(militaryFormation);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        CompanyResponse companyResponse = modelMapper.toCompanyResponse(company);
        companyRepository.deleteById(id);
        return companyResponse;
    }

    @Transactional
    public CompanyResponse create(CompanyDto companyDto) {
        Company company = modelMapper.toCompany(companyDto);
        company.setPlatoons(new ArrayList<>());

        Soldier soldier = null;
        if(companyDto.getCommanderId() != null){
            soldier = solderRepository.findById(companyDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        company.setCommander(soldier);

        MilitaryFormation militaryFormation = null;
        if(companyDto.getFormationId() != null){
            militaryFormation = militaryFormationRepository.findById(companyDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));
        }
        company.setFormation(militaryFormation);


        if(companyDto.getPlatoonsId() != null){
            for(Long ids : companyDto.getPlatoonsId()){
                Platoon platoon = platoonRepository.findById(ids).orElseThrow(() -> new Error404("Company not found"));
                platoon.setCompany(company);
                company.getPlatoons().add(platoon);

                try{
                    platoonRepository.save(platoon);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            company = companyRepository.saveAndFlush(company);
            if(soldier != null){
                soldier.setCommandedUnit(company);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toCompanyResponse(company);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public CompanyResponse update(CompanyDto companyDto, long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new Error404("Company not found"));

        for(Platoon platoon : company.getPlatoons()){
            platoon.setCompany(null);
            try{
                platoonRepository.saveAndFlush(platoon);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        company.setPlatoons(new ArrayList<>());

        Soldier soldier = null;
        if(companyDto.getCommanderId() != null){
            soldier = solderRepository.findById(companyDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
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
        company.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(company);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        MilitaryFormation militaryFormation = null;
        if(companyDto.getFormationId() != null){
            militaryFormation = militaryFormationRepository.findById(companyDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));
        }
        company.setFormation(militaryFormation);


        if(companyDto.getPlatoonsId() != null){
            for(Long ids : companyDto.getPlatoonsId()){
                Platoon platoon = platoonRepository.findById(ids).orElseThrow(() -> new Error404("Company not found"));
                platoon.setCompany(company);
                company.getPlatoons().add(platoon);

                try{
                    platoonRepository.save(platoon);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        company.setName(companyDto.getName());

        try{
            company = companyRepository.save(company);
            return modelMapper.toCompanyResponse(company);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
