package boev.app.services.impl.armaments.type.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.fireHelp.AntiaircraftType;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeDto;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeResponse;
import boev.app.repository.armaments.type.AntiaircraftTypeRepository;
import boev.app.services.interfaces.armaments.type.fireHelp.AntiaircraftTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AntiaircraftTypeServiceImpl implements AntiaircraftTypeService {
    private final AntiaircraftTypeRepository antiaircraftTypeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<AntiaircraftTypeResponse> getAll() {
        return antiaircraftTypeRepository.findAll().stream().map(modelMapper::toAntiaircraftTypeResponse).collect(Collectors.toList());
    }

    @Transactional
    public AntiaircraftTypeResponse get(long id) {
        return modelMapper.toAntiaircraftTypeResponse(antiaircraftTypeRepository.findById(id).orElseThrow(() -> new Error404("AntiaircraftType not found")));
    }

    @Transactional
    public AntiaircraftTypeResponse delete(long id) {
        AntiaircraftTypeResponse antiaircraftTypeResponse = modelMapper.toAntiaircraftTypeResponse(antiaircraftTypeRepository.findById(id).orElseThrow(() -> new Error404("AntiaircraftType not found")));
        antiaircraftTypeRepository.deleteById(id);
        return antiaircraftTypeResponse;
    }

    @Transactional
    public AntiaircraftTypeResponse update(long id, AntiaircraftTypeDto antiaircraftTypeDto) {
        AntiaircraftType antiaircraftType = antiaircraftTypeRepository.findById(id).orElseThrow(() -> new Error404("AntiaircraftType not found"));

        antiaircraftType.setModel(antiaircraftType.getModel());
        antiaircraftType.setRange(antiaircraftTypeDto.getRange());
        antiaircraftType.setTargetAltitude(antiaircraftTypeDto.getTargetAltitude());
        try{
            antiaircraftType = antiaircraftTypeRepository.save(antiaircraftType);
            return modelMapper.toAntiaircraftTypeResponse(antiaircraftType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public AntiaircraftTypeResponse create(AntiaircraftTypeDto antiaircraftTypeDto) {
        AntiaircraftType antiaircraftType = modelMapper.toAntiaircraftType(antiaircraftTypeDto);
        try{
            antiaircraftType = antiaircraftTypeRepository.save(antiaircraftType);
            return modelMapper.toAntiaircraftTypeResponse(antiaircraftType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
