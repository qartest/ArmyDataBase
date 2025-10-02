package boev.app.services.impl.armaments.type.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.technique.TankType;
import boev.app.payload.armaments.type.technique.Tank.TankTypeDto;
import boev.app.payload.armaments.type.technique.Tank.TankTypeResponse;
import boev.app.repository.armaments.type.TankTypeRepository;
import boev.app.services.interfaces.armaments.type.technique.TankTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TankTypeServiceImpl implements TankTypeService {
    private final TankTypeRepository tankTypeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<TankTypeResponse> getAll() {
        return tankTypeRepository.findAll().stream().map(modelMapper::toTankTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TankTypeResponse get(long id) {
        return modelMapper.toTankTypeResponse(tankTypeRepository.findById(id).orElseThrow(() -> new Error404("TankType not found")));
    }

    @Transactional
    public TankTypeResponse delete(long id) {
        TankTypeResponse tankTypeResponse = modelMapper.toTankTypeResponse(tankTypeRepository.findById(id).orElseThrow(() -> new Error404("TankType not found")));
        tankTypeRepository.deleteById(id);
        return tankTypeResponse;
    }

    @Transactional
    public TankTypeResponse create(TankTypeDto tankTypeDto) {
        TankType tankType = modelMapper.toTankType(tankTypeDto);

        try{
            tankType = tankTypeRepository.save(tankType);
            return modelMapper.toTankTypeResponse(tankType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public TankTypeResponse update(long id, TankTypeDto tankTypeDto) {
        TankType tankType = tankTypeRepository.findById(id).orElseThrow(() -> new Error404("TankType not found"));

        tankType.setModel(tankTypeDto.getModel());
        tankType.setWeight(tankTypeDto.getWeight());
        tankType.setMaxSpeed(tankTypeDto.getMaxSpeed());
        tankType.setGunCaliber(tankTypeDto.getGunCaliber());
        try{
            tankType = tankTypeRepository.save(tankType);
            return modelMapper.toTankTypeResponse(tankType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
