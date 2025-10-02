package boev.app.services.impl.armaments.type.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.technique.TruckType;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;
import boev.app.repository.armaments.type.TruckTypeRepository;
import boev.app.services.interfaces.armaments.type.technique.TruckTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TruckTypeServiceImpl implements TruckTypeService {
    private final ModelMapper modelMapper;
    private final TruckTypeRepository truckTypeRepository;

    @Transactional
    public List<TruckTypeResponse> getAll() {
        return truckTypeRepository.findAll().stream().map(modelMapper::toTruckTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TruckTypeResponse get(long id) {
        return modelMapper.toTruckTypeResponse(truckTypeRepository.findById(id).orElseThrow(() -> new Error404("TruckType not found")));
    }

    @Transactional
    public TruckTypeResponse delete(long id) {
        TruckTypeResponse truckTypeResponse = modelMapper.toTruckTypeResponse(truckTypeRepository.findById(id).orElseThrow(() -> new Error404("TruckType not found")));
        truckTypeRepository.deleteById(id);
        return truckTypeResponse;
    }

    @Transactional
    public TruckTypeResponse update(long id, TruckTypeDto truckTypeDto) {
        TruckType truckType = truckTypeRepository.findById(id).orElseThrow(() -> new Error404("TruckType not found"));

        truckType.setModel(truckTypeDto.getModel());
        truckType.setPayloadCapacity(truckTypeDto.getPayloadCapacity());
        truckType.setPassengerCapacity(truckTypeDto.getPassengerCapacity());
        try{
            truckType = truckTypeRepository.save(truckType);
            return modelMapper.toTruckTypeResponse(truckType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public TruckTypeResponse create(TruckTypeDto truckTypeDto) {
        TruckType truckType = modelMapper.toTruckType(truckTypeDto);

        try{
            truckType = truckTypeRepository.save(truckType);
            return modelMapper.toTruckTypeResponse(truckType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
