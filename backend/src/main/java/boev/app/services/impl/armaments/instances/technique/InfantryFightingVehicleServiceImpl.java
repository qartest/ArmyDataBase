package boev.app.services.impl.armaments.instances.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.technique.InfantryFightingVehicle;
import boev.app.models.armaments.type.technique.InfantryFightingVehicleType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleDto;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleResponse;
import boev.app.repository.armaments.instatnces.InfantryFightingVehicleRepository;
import boev.app.repository.armaments.type.InfantryFightingVehicleTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.technique.InfantryFightingVehicleService;
import boev.app.services.interfaces.armaments.type.technique.InfantryFightingVehicleTypeService;
import ch.qos.logback.core.model.Model;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InfantryFightingVehicleServiceImpl implements InfantryFightingVehicleService {
    private final ModelMapper modelMapper;
    private final InfantryFightingVehicleRepository infantryFightingVehicleRepository;
    private final InfantryFightingVehicleTypeRepository infantryFightingVehicleTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;

    @Transactional
    public List<InfantryFightingVehicleResponse> getAll() {
        return infantryFightingVehicleRepository.findAll().stream().map(modelMapper::toInfantryFightingVehicleResponse).collect(Collectors.toList());
    }

    @Transactional
    public InfantryFightingVehicleResponse get(long id) {
        return modelMapper.toInfantryFightingVehicleResponse(infantryFightingVehicleRepository.findById(id).orElseThrow(() -> new Error404("InfantryFightingVehicle not found")));
    }

    @Transactional
    public InfantryFightingVehicleResponse delete(long id) {
        InfantryFightingVehicleResponse infantryFightingVehicleResponse = modelMapper.toInfantryFightingVehicleResponse(infantryFightingVehicleRepository.findById(id).orElseThrow(() -> new Error404("InfantryFightingVehicle not found")));
        infantryFightingVehicleRepository.deleteById(id);
        return infantryFightingVehicleResponse;
    }

    @Transactional
    public InfantryFightingVehicleResponse create(InfantryFightingVehicleDto infantryFightingVehicleDto) {
        InfantryFightingVehicle infantryFightingVehicle = modelMapper.toInfantryFightingVehicle(infantryFightingVehicleDto);
        InfantryFightingVehicleType infantryFightingVehicleType = infantryFightingVehicleTypeRepository.findById(infantryFightingVehicleDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(infantryFightingVehicleDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        infantryFightingVehicle.setFormation(militaryFormation);
        infantryFightingVehicle.setType(infantryFightingVehicleType);

        try{
            infantryFightingVehicle = infantryFightingVehicleRepository.save(infantryFightingVehicle);
            return modelMapper.toInfantryFightingVehicleResponse(infantryFightingVehicle);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public InfantryFightingVehicleResponse update(long id, InfantryFightingVehicleDto infantryFightingVehicleDto) {
        InfantryFightingVehicle infantryFightingVehicle = infantryFightingVehicleRepository.findById(id).orElseThrow(() -> new Error404("InfantryFightingVehicle not found"));
        InfantryFightingVehicleType infantryFightingVehicleType = infantryFightingVehicleTypeRepository.findById(infantryFightingVehicleDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(infantryFightingVehicleDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));


        infantryFightingVehicle.setType(infantryFightingVehicleType);
        infantryFightingVehicle.setFormation(militaryFormation);
        infantryFightingVehicle.setVehicleCondition(infantryFightingVehicleDto.getVehicleCondition());
        infantryFightingVehicle.setSerialNumber(infantryFightingVehicleDto.getSerialNumber());
        infantryFightingVehicle.setYearOfManufacture(infantryFightingVehicleDto.getYearOfManufacture());

        try{
            infantryFightingVehicle = infantryFightingVehicleRepository.save(infantryFightingVehicle);
            return modelMapper.toInfantryFightingVehicleResponse(infantryFightingVehicle);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
