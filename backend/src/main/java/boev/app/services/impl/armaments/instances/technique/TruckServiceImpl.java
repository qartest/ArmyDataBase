package boev.app.services.impl.armaments.instances.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.technique.Truck;
import boev.app.models.armaments.type.technique.TruckType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.technique.Truck.TruckDto;
import boev.app.payload.armaments.instances.technique.Truck.TruckResponse;
import boev.app.repository.armaments.instatnces.TruckRepository;
import boev.app.repository.armaments.type.TruckTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.technique.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;
    private final TruckTypeRepository truckTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;

    private final ModelMapper modelMapper;


    @Transactional
    public List<TruckResponse> getAll() {
        return truckRepository.findAll().stream().map(modelMapper::toTruckResponse).collect(Collectors.toList());
    }

    @Transactional
    public TruckResponse get(long id) {
        return modelMapper.toTruckResponse(truckRepository.findById(id).orElseThrow(() -> new Error404("Truck not found")));
    }

    @Transactional
    public TruckResponse update(TruckDto truckDto, long id) {
        Truck truck = truckRepository.findById(id).orElseThrow(() -> new Error404("Truck not found"));
        TruckType truckType = truckTypeRepository.findById(truckDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(truckDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        truck.setFormation(militaryFormation);
        truck.setType(truckType);

        truck.setMileage(truckDto.getMileage());
        truck.setSerialNumber(truckDto.getSerialNumber());
        truck.setYearOfManufacture(truckDto.getYearOfManufacture());

        try{
            truck = truckRepository.save(truck);
            return modelMapper.toTruckResponse(truck);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public TruckResponse delete(long id) {
        TruckResponse truckResponse = modelMapper.toTruckResponse(truckRepository.findById(id).orElseThrow(() -> new Error404("Truck not found")));
        truckRepository.deleteById(id);
        return truckResponse;
    }

    @Transactional
    public TruckResponse create(TruckDto truckDto) {
        Truck truck = modelMapper.toTruck(truckDto);
        TruckType truckType = truckTypeRepository.findById(truckDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(truckDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        truck.setFormation(militaryFormation);
        truck.setType(truckType);

        try{
            truck = truckRepository.save(truck);
            return modelMapper.toTruckResponse(truck);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
