package boev.app.services.impl.armaments.instances.weapon;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.weapon.AutomaticWeapon;
import boev.app.models.armaments.type.weapon.AutomaticWeaponType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponDto;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponResponse;
import boev.app.repository.armaments.instatnces.AutomaticWeaponRepository;
import boev.app.repository.armaments.type.AutomaticWeaponTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.weapon.AutomaticWeaponService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutomaticWeaponServiceImpl implements AutomaticWeaponService {
    private final AutomaticWeaponRepository automaticWeaponRepository;
    private final AutomaticWeaponTypeRepository automaticWeaponTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<AutomaticWeaponResponse> getAll() {
        return automaticWeaponRepository.findAll().stream().map(modelMapper::toAutomaticWeaponResponse).collect(Collectors.toList());
    }

    @Transactional
    public AutomaticWeaponResponse get(long id) {
        return modelMapper.toAutomaticWeaponResponse(automaticWeaponRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeapon not found")));
    }

    @Transactional
    public AutomaticWeaponResponse create(AutomaticWeaponDto automaticWeaponDto) {
        AutomaticWeapon automaticWeapon = modelMapper.toAutomaticWeapon(automaticWeaponDto);
        AutomaticWeaponType automaticWeaponType = automaticWeaponTypeRepository.findById(automaticWeaponDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(automaticWeaponDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        automaticWeapon.setFormation(militaryFormation);
        automaticWeapon.setType(automaticWeaponType);

        try{
            automaticWeapon = automaticWeaponRepository.save(automaticWeapon);
            return modelMapper.toAutomaticWeaponResponse(automaticWeapon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public AutomaticWeaponResponse update(long id, AutomaticWeaponDto automaticWeaponDto) {
        AutomaticWeapon automaticWeapon = automaticWeaponRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeapon not found"));
        AutomaticWeaponType automaticWeaponType = automaticWeaponTypeRepository.findById(automaticWeaponDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(automaticWeaponDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        automaticWeapon.setSerialNumber(automaticWeaponDto.getSerialNumber());
        automaticWeapon.setStockCondition(automaticWeaponDto.getStockCondition());
        automaticWeapon.setYearOfManufacture(automaticWeaponDto.getYearOfManufacture());
        automaticWeapon.setFormation(militaryFormation);
        automaticWeapon.setType(automaticWeaponType);
        try{
            automaticWeapon = automaticWeaponRepository.save(automaticWeapon);
            return modelMapper.toAutomaticWeaponResponse(automaticWeapon);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public AutomaticWeaponResponse delete(long id) {
        AutomaticWeaponResponse automaticWeaponResponse = modelMapper.toAutomaticWeaponResponse(automaticWeaponRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeapon not found")));
        automaticWeaponRepository.deleteById(id);
        return automaticWeaponResponse;
    }
}
