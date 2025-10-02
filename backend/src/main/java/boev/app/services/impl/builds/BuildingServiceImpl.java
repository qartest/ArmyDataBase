package boev.app.services.impl.builds;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.builds.Building;
import boev.app.models.builds.Headquarter;
import boev.app.payload.builds.building.BuildingDto;
import boev.app.payload.builds.building.BuildingResponse;
import boev.app.repository.builds.BuildingRepository;
import boev.app.repository.builds.HeadquarterRepository;
import boev.app.services.interfaces.builds.BuildingService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final ModelMapper modelMapper;
    private final BuildingRepository buildingRepository;
    private final HeadquarterRepository headquarterRepository;

    @Transactional
    public List<BuildingResponse> getAll() {
        return buildingRepository.findAll().stream().map(modelMapper::toBuildingResponse).collect(Collectors.toList());
    }

    @Transactional
    public BuildingResponse get(long id) {
        return modelMapper.toBuildingResponse(buildingRepository.findById(id).orElseThrow(() -> new Error404("Building not found")));
    }

    @Transactional
    public BuildingResponse update(long id, BuildingDto buildingDto) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new Error404("Building not found"));

        Headquarter headquarter = null;
        if(buildingDto.getHeadquarterId() != null){
            headquarter = headquarterRepository.findById(buildingDto.getHeadquarterId()).orElseThrow(() -> new Error404("Headquarter not found"));
        }

        building.setHeadquarter(headquarter);
        building.setType(buildingDto.getType());
        building.setAddress(buildingDto.getAddress());


        try{
            building = buildingRepository.save(building);
            return modelMapper.toBuildingResponse(building);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public BuildingResponse create(BuildingDto buildingDto) {
        Building building = modelMapper.toBuilding(buildingDto);
        Headquarter headquarter = null;
        if(buildingDto.getHeadquarterId() != null){
            headquarter = headquarterRepository.findById(buildingDto.getHeadquarterId()).orElseThrow(() -> new Error404("Headquarter not found"));
        }

        building.setHeadquarter(headquarter);

        try{
            building = buildingRepository.save(building);
            return modelMapper.toBuildingResponse(building);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public BuildingResponse delete(long id) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new Error404("Building not found"));

        if(building.getHeadquarter() != null){
            Headquarter headquarter = building.getHeadquarter();

            headquarter.getBuildings().removeIf(s -> s.getId() == id);
            try{
                headquarterRepository.save(headquarter);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        BuildingResponse buildingResponse  = modelMapper.toBuildingResponse(building);
        buildingRepository.deleteById(id);
        return buildingResponse;
    }
}
