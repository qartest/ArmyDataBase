package boev.app.services.impl.builds;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.builds.Building;
import boev.app.models.builds.Headquarter;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.builds.headquater.HeadquarterDto;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.builds.headquater.HeadquarterResponseSimple;
import boev.app.repository.builds.BuildingRepository;
import boev.app.repository.builds.HeadquarterRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.builds.HeadquarterService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HeadquarterServiceImpl implements HeadquarterService {
    private final ModelMapper modelMapper;
    private final HeadquarterRepository headquarterRepository;
    private final BuildingRepository buildingRepository;
    private final MilitaryFormationRepository militaryFormationRepository;

    @Transactional
    public List<HeadquarterResponseSimple> getAllSimple() {
        return headquarterRepository.findAll().stream().map(modelMapper::toHeadquarterResponseSimple).collect(Collectors.toList());
    }

    @Transactional
    public List<HeadquarterResponse> getAll() {
        return headquarterRepository.findAll().stream().map(modelMapper::toHeadquarterResponse).collect(Collectors.toList());
    }
    @Transactional
    public HeadquarterResponse get(long id) {
        return modelMapper.toHeadquarterResponse(headquarterRepository.findById(id).orElseThrow(() -> new Error404("Headquarter not found")));
    }

    @Transactional
    public HeadquarterResponse create(HeadquarterDto headquarterDto) {
        Headquarter headquarter = modelMapper.toHeadquarter(headquarterDto);
        headquarter.setBuildings(new ArrayList<>());
        headquarter.setFormations(new ArrayList<>());

        try{
            headquarter = headquarterRepository.save(headquarter);
            return modelMapper.toHeadquarterResponse(headquarter);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public HeadquarterResponse delete(long id) {
        Headquarter headquarter = headquarterRepository.findById(id).orElseThrow(() -> new Error404("Headquarter not found"));

        if(headquarter.getBuildings() != null){
            for(Building building : headquarter.getBuildings()){
                building.setHeadquarter(null);

                try{
                    buildingRepository.save(building);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(headquarter.getFormations() != null){
            for(MilitaryFormation militaryFormation : headquarter.getFormations()){
                militaryFormation.setHeadquarter(null);

                try{
                    militaryFormationRepository.save(militaryFormation);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        HeadquarterResponse headquarterResponse = modelMapper.toHeadquarterResponse(headquarter);
        headquarterRepository.deleteById(id);
        return headquarterResponse;
    }

    @Transactional
    public HeadquarterResponse update(HeadquarterDto headquarterDto, long id) {
        Headquarter headquarter = headquarterRepository.findById(id).orElseThrow(() -> new Error404("Headquarter not found"));

        headquarter.setAddress(headquarterDto.getAddress());
        headquarter.setName(headquarterDto.getName());
        try{
            headquarter = headquarterRepository.save(headquarter);
            return modelMapper.toHeadquarterResponse(headquarter);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
