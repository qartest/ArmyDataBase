package boev.app.services.impl.armaments.instances.weapon;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.weapon.Carbine;
import boev.app.models.armaments.type.weapon.CarbineType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineDto;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineResponse;
import boev.app.repository.armaments.instatnces.CarbineRepository;
import boev.app.repository.armaments.type.CarbineTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.weapon.CarbineService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CarbineServiceImpl implements CarbineService {
    private final CarbineRepository carbineRepository;
    private final CarbineTypeRepository carbineTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public List<CarbineResponse> getAll() {
        return carbineRepository.findAll().stream().map(modelMapper::toCarbineResponse).collect(Collectors.toList());
    }

    @Transactional
    public CarbineResponse get(long id) {
        return modelMapper.toCarbineResponse(carbineRepository.findById(id).orElseThrow(() -> new Error404("Carbine not found")));
    }

    @Transactional
    public CarbineResponse delete(long id) {
        CarbineResponse carbineResponse = modelMapper.toCarbineResponse(carbineRepository.findById(id).orElseThrow(() -> new Error404("Carbine not found")));
        carbineRepository.deleteById(id);
        return carbineResponse;
    }

    @Transactional
    public CarbineResponse update(CarbineDto carbineDto, long id) {
        Carbine carbine = carbineRepository.findById(id).orElseThrow(() -> new Error404("Carbine not found"));
        CarbineType carbineType = carbineTypeRepository.findById(carbineDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(carbineDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        carbine.setKills(carbineDto.getKills());
        carbine.setSerialNumber(carbineDto.getSerialNumber());
        carbine.setSightCondition(carbineDto.getSightCondition());
        carbine.setYearOfManufacture(carbineDto.getYearOfManufacture());
        carbine.setFormation(militaryFormation);
        carbine.setType(carbineType);

        try{
            carbine = carbineRepository.save(carbine);
            return modelMapper.toCarbineResponse(carbine);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public CarbineResponse create(CarbineDto carbineDto) {
        Carbine carbine = modelMapper.toCarbine(carbineDto);
        CarbineType carbineType = carbineTypeRepository.findById(carbineDto.getTypeId()).orElseThrow(() -> new Error404("Type not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(carbineDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        carbine.setFormation(militaryFormation);
        carbine.setType(carbineType);

        try{
            carbine = carbineRepository.save(carbine);
            return modelMapper.toCarbineResponse(carbine);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
