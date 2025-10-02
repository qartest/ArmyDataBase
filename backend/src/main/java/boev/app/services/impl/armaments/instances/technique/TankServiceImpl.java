package boev.app.services.impl.armaments.instances.technique;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.technique.Tank;
import boev.app.models.armaments.type.technique.TankType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.technique.Tank.TankDto;
import boev.app.payload.armaments.instances.technique.Tank.TankResponse;
import boev.app.repository.armaments.instatnces.TankRepository;
import boev.app.repository.armaments.type.TankTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.technique.TankService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TankServiceImpl implements TankService {
    private final ModelMapper modelMapper;
    private final TankRepository tankRepository;
    private final TankTypeRepository tankTypeRepository;
    private  final MilitaryFormationRepository militaryFormationRepository;

    @Transactional
    public List<TankResponse> getAll() {
        return tankRepository.findAll().stream().map(modelMapper::toTankResponse).collect(Collectors.toList());
    }

    @Transactional
    public TankResponse get(long id) {
        return modelMapper.toTankResponse(tankRepository.findById(id).orElseThrow(() -> new Error404("Tank not found")));
    }

    @Transactional
    public TankResponse create(TankDto tankDto) {
        Tank tank = modelMapper.toTank(tankDto);
        TankType type = tankTypeRepository.findById(tankDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(tankDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        tank.setType(type);
        tank.setFormation(militaryFormation);

        try{
            tank = tankRepository.save(tank);
            return modelMapper.toTankResponse(tank);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public TankResponse delete(long id) {
        TankResponse tankResponse = modelMapper.toTankResponse(tankRepository.findById(id).orElseThrow(() -> new Error404("Tank not found")));
        tankRepository.deleteById(id);
        return tankResponse;
    }

    @Transactional
    public TankResponse update(TankDto tankDto, long id) {
        Tank tank = tankRepository.findById(id).orElseThrow(() -> new Error404("Tank not found"));
        TankType type = tankTypeRepository.findById(tankDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(tankDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        tank.setType(type);
        tank.setFormation(militaryFormation);
        tank.setTankKills(tankDto.getTankKills());
        tank.setGunStatus(tankDto.getGunStatus());
        tank.setTrackStatus(tankDto.getTrackStatus());
        tank.setSerialNumber(tankDto.getSerialNumber());
        tank.setYearOfManufacture(tankDto.getYearOfManufacture());
        tank.setSerialNumber(tankDto.getSerialNumber());

        try{
            tank = tankRepository.save(tank);
            return modelMapper.toTankResponse(tank);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }
}
