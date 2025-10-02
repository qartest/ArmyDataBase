package boev.app.services.impl.armaments.type.weapon;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.weapon.AutomaticWeaponType;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeDto;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeResponse;
import boev.app.repository.armaments.type.AutomaticWeaponTypeRepository;
import boev.app.services.interfaces.armaments.type.weapon.AutomaticWeaponTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AutomaticWeaponTypeServiceImpl implements AutomaticWeaponTypeService {
    private final ModelMapper modelMapper;
    private final AutomaticWeaponTypeRepository automaticWeaponTypeRepository;

    @Transactional
    public List<AutomaticWeaponTypeResponse> getAll() {
        return automaticWeaponTypeRepository.findAll().stream().map(modelMapper::toAutomaticWeaponTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public AutomaticWeaponTypeResponse get(long id) {
        return modelMapper.toAutomaticWeaponTypeResponse(automaticWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeaponType not found")));
    }

    @Transactional
    public AutomaticWeaponTypeResponse delete(long id) {
        AutomaticWeaponTypeResponse automaticWeaponTypeResponse = modelMapper.toAutomaticWeaponTypeResponse(automaticWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeaponType not found")));
        automaticWeaponTypeRepository.deleteById(id);
        return automaticWeaponTypeResponse;
    }

    @Transactional
    public AutomaticWeaponTypeResponse update(long id, AutomaticWeaponTypeDto automaticWeaponTypeDto) {
        AutomaticWeaponType automaticWeaponType = automaticWeaponTypeRepository.findById(id).orElseThrow(() -> new Error404("AutomaticWeaponType not found"));

        automaticWeaponType.setModel(automaticWeaponTypeDto.getModel());
        automaticWeaponType.setCaliber(automaticWeaponTypeDto.getCaliber());
        automaticWeaponType.setFireRate(automaticWeaponTypeDto.getFireRate());
        automaticWeaponType.setMagazineCapacity(automaticWeaponTypeDto.getMagazineCapacity());

        try{
            automaticWeaponType = automaticWeaponTypeRepository.save(automaticWeaponType);
            return modelMapper.toAutomaticWeaponTypeResponse(automaticWeaponType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public AutomaticWeaponTypeResponse create(AutomaticWeaponTypeDto automaticWeaponTypeDto) {
        AutomaticWeaponType automaticWeaponType = modelMapper.toAutomaticWeaponType(automaticWeaponTypeDto);

        try{
            automaticWeaponType = automaticWeaponTypeRepository.save(automaticWeaponType);
            return modelMapper.toAutomaticWeaponTypeResponse(automaticWeaponType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
