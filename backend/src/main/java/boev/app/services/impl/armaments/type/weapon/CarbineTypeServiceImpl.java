package boev.app.services.impl.armaments.type.weapon;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.weapon.CarbineType;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeResponse;
import boev.app.repository.armaments.type.CarbineTypeRepository;
import boev.app.services.interfaces.armaments.type.weapon.CarbineTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarbineTypeServiceImpl implements CarbineTypeService {
    private final ModelMapper modelMapper;
    private final CarbineTypeRepository carbineTypeRepository;

    @Transactional
    public List<CarbineTypeResponse> getAll() {
        return carbineTypeRepository.findAll().stream().map(modelMapper::toCarbineTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public CarbineTypeResponse get(long id) {
        return modelMapper.toCarbineTypeResponse(carbineTypeRepository.findById(id).orElseThrow(() -> new Error404("CarbineType not found")));
    }

    @Transactional
    public CarbineTypeResponse delete(long id) {
        CarbineTypeResponse carbineTypeResponse = modelMapper.toCarbineTypeResponse(carbineTypeRepository.findById(id).orElseThrow(() -> new Error404("CarbineType not found")));
        carbineTypeRepository.deleteById(id);
        return carbineTypeResponse;
    }

    @Transactional
    public CarbineTypeResponse update(long id, CarbineTypeDto carbineTypeDto) {
        CarbineType carbineType = carbineTypeRepository.findById(id).orElseThrow(() -> new Error404("CarbineType not found"));

        carbineType.setModel(carbineTypeDto.getModel());
        carbineType.setCaliber(carbineTypeDto.getCaliber());
        carbineType.setWeight(carbineTypeDto.getWeight());
        carbineType.setEffectiveRange(carbineTypeDto.getEffectiveRange());

        try{
            carbineType = carbineTypeRepository.save(carbineType);
            return modelMapper.toCarbineTypeResponse(carbineType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public CarbineTypeResponse create(CarbineTypeDto carbineTypeDto) {
        CarbineType carbineType = modelMapper.toCarbineType(carbineTypeDto);

        try{
            carbineType = carbineTypeRepository.save(carbineType);
            return modelMapper.toCarbineTypeResponse(carbineType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
