package boev.app.services.impl.armaments.type.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.fireHelp.MissileWeaponType;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeDto;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeResponse;
import boev.app.repository.armaments.type.MissileWeaponTypeRepository;
import boev.app.services.interfaces.armaments.type.fireHelp.MissileWeaponTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MissileWeaponTypeServiceImpl implements MissileWeaponTypeService {
    private final ModelMapper modelMapper;
    private final MissileWeaponTypeRepository missileWeaponTypeRepository;

    @Override
    public List<MissileWeaponTypeResponse> getAll() {
        return missileWeaponTypeRepository.findAll().stream().map(modelMapper::toMissileWeaponTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public MissileWeaponTypeResponse get(long id) {
        return modelMapper.toMissileWeaponTypeResponse(missileWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("MissileWeaponType not found")));
    }

    @Transactional
    public MissileWeaponTypeResponse update(MissileWeaponTypeDto missileWeaponTypeDto, long id) {
        MissileWeaponType missileWeaponType = missileWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("MissileWeaponType not found"));

        missileWeaponType.setWarheadType(missileWeaponTypeDto.getWarheadType());
        missileWeaponType.setRange(missileWeaponTypeDto.getRange());
        missileWeaponType.setModel(missileWeaponTypeDto.getModel());
        missileWeaponType.setGuidanceSystem(missileWeaponTypeDto.getGuidanceSystem());
        try{
            missileWeaponType = missileWeaponTypeRepository.save(missileWeaponType);
            return modelMapper.toMissileWeaponTypeResponse(missileWeaponType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public MissileWeaponTypeResponse create(MissileWeaponTypeDto missileWeaponTypeDto) {
        MissileWeaponType missileWeaponType = modelMapper.toMissileWeaponType(missileWeaponTypeDto);

        try{
            missileWeaponType = missileWeaponTypeRepository.save(missileWeaponType);
            return modelMapper.toMissileWeaponTypeResponse(missileWeaponType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public MissileWeaponTypeResponse delete(long id) {
        MissileWeaponTypeResponse missileWeaponTypeResponse = modelMapper.toMissileWeaponTypeResponse(missileWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("MissileWeaponType not found")));
        missileWeaponTypeRepository.deleteById(id);
        return missileWeaponTypeResponse;
    }
}
