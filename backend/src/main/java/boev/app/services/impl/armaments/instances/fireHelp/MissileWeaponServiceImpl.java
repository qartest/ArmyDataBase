package boev.app.services.impl.armaments.instances.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.fireHelp.MissileWeapon;
import boev.app.models.armaments.type.fireHelp.MissileWeaponType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponDto;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponResponse;
import boev.app.repository.armaments.instatnces.MissileWeaponRepository;
import boev.app.repository.armaments.type.MissileWeaponTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.fireHelp.MissileWeaponService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class MissileWeaponServiceImpl implements MissileWeaponService {
    private final ModelMapper modelMapper;
    private final MissileWeaponRepository missileWeaponRepository;
    private final MissileWeaponTypeRepository missileWeaponTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;


    @Transactional
    public List<MissileWeaponResponse> getAll() {
        return missileWeaponRepository.findAll().stream().map(modelMapper::toMissileWeaponResponse).collect(Collectors.toList());
    }

    @Transactional
    public MissileWeaponResponse createMissileWeapon(MissileWeaponDto missileWeaponDto) {
        MissileWeapon missileWeapon = modelMapper.toMissileWeapon(missileWeaponDto);
        MissileWeaponType missileWeaponType = missileWeaponTypeRepository.findById(missileWeaponDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(missileWeaponDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        missileWeapon.setType(missileWeaponType);
        missileWeapon.setFormation(militaryFormation);

        try{
            missileWeapon = missileWeaponRepository.save(missileWeapon);
            return modelMapper.toMissileWeaponResponse(missileWeapon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public MissileWeaponResponse deleteMissileWeapon(long id) {
        MissileWeaponResponse missileWeaponResponse = modelMapper.toMissileWeaponResponse(missileWeaponRepository.findById(id).orElseThrow(() -> new Error404("MissileWeapon not found")));
        missileWeaponRepository.deleteById(id);
        return missileWeaponResponse;
    }

    @Transactional
    public MissileWeaponResponse updateMissileWeapon(MissileWeaponDto missileWeaponDto, long id) {
        MissileWeapon missileWeapon = missileWeaponRepository.findById(id).orElseThrow(() -> new Error404("MissileWeapon not found"));
        MissileWeaponType missileWeaponType = missileWeaponTypeRepository.findById(missileWeaponDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(missileWeaponDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        missileWeapon.setFormation(militaryFormation);
        missileWeapon.setType(missileWeaponType);
        missileWeapon.setCategory(missileWeaponDto.getCategory());
        missileWeapon.setSerialNumber(missileWeaponDto.getSerialNumber());
        missileWeapon.setLauncherStatus(missileWeaponDto.getLauncherStatus());
        missileWeapon.setYearOfManufacture(missileWeaponDto.getYearOfManufacture());

        try{
            missileWeapon = missileWeaponRepository.save(missileWeapon);
            return modelMapper.toMissileWeaponResponse(missileWeapon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public MissileWeaponResponse getMissileWeapon(Long id) {
        return modelMapper.toMissileWeaponResponse(missileWeaponRepository.findById(id).orElseThrow(() -> new Error404("MissileWeapon not found")));
    }
}
