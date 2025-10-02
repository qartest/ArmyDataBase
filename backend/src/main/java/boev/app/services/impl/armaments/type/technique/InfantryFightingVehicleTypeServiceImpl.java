package boev.app.services.impl.armaments.type.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.technique.InfantryFightingVehicleType;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeDto;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeResponse;
import boev.app.repository.armaments.type.InfantryFightingVehicleTypeRepository;
import boev.app.services.interfaces.armaments.type.technique.InfantryFightingVehicleTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InfantryFightingVehicleTypeServiceImpl implements InfantryFightingVehicleTypeService {
    private final InfantryFightingVehicleTypeRepository infantryFightingVehicleTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<InfantryFightingVehicleTypeResponse> getAll() {
        return infantryFightingVehicleTypeRepository.findAll().stream().map(modelMapper::toInfantryFightingVehicleTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public InfantryFightingVehicleTypeResponse get(long id) {
        return modelMapper.toInfantryFightingVehicleTypeResponse(infantryFightingVehicleTypeRepository.findById(id).orElseThrow(() -> new Error404("IFVType not found")));
    }

    @Transactional
    public InfantryFightingVehicleTypeResponse delete(long id) {
        InfantryFightingVehicleTypeResponse infantryFightingVehicleTypeResponse = modelMapper.toInfantryFightingVehicleTypeResponse(infantryFightingVehicleTypeRepository.findById(id).orElseThrow(() -> new Error404("IFVType not found")));
        infantryFightingVehicleTypeRepository.deleteById(id);
        return infantryFightingVehicleTypeResponse;
    }

    @Transactional
    public InfantryFightingVehicleTypeResponse update(long id, InfantryFightingVehicleTypeDto infantryFightingVehicleTypeDto) {
        InfantryFightingVehicleType infantryFightingVehicleType = infantryFightingVehicleTypeRepository.findById(id).orElseThrow(() -> new Error404("IFVType not found"));

        infantryFightingVehicleType.setModel(infantryFightingVehicleTypeDto.getModel());
        infantryFightingVehicleType.setArmorThickness(infantryFightingVehicleTypeDto.getArmorThickness());
        infantryFightingVehicleType.setGunCaliber(infantryFightingVehicleTypeDto.getGunCaliber());
        infantryFightingVehicleType.setPassengerCapacity(infantryFightingVehicleTypeDto.getPassengerCapacity());

        try{
            infantryFightingVehicleType = infantryFightingVehicleTypeRepository.save(infantryFightingVehicleType);
            return modelMapper.toInfantryFightingVehicleTypeResponse(infantryFightingVehicleType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public InfantryFightingVehicleTypeResponse create(InfantryFightingVehicleTypeDto infantryFightingVehicleTypeDto) {
        InfantryFightingVehicleType infantryFightingVehicleType = modelMapper.toInfantryFightingVehicleType(infantryFightingVehicleTypeDto);

        try{
            infantryFightingVehicleType = infantryFightingVehicleTypeRepository.save(infantryFightingVehicleType);
            return modelMapper.toInfantryFightingVehicleTypeResponse(infantryFightingVehicleType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
