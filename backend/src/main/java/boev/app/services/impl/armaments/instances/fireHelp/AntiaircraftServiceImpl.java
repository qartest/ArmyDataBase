package boev.app.services.impl.armaments.instances.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.type.fireHelp.AntiaircraftType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftDto;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;
import boev.app.repository.armaments.instatnces.AntiaircraftRepository;
import boev.app.repository.armaments.type.AntiaircraftTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.fireHelp.AntiaircraftService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AntiaircraftServiceImpl implements AntiaircraftService {
    private final AntiaircraftRepository antiaircraftRepository;
    private final ModelMapper modelMapper;
    private final AntiaircraftTypeRepository antiaircraftTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;

    @Transactional
    public List<AntiaircraftResponse> getAll() {
        return antiaircraftRepository.findAll().stream().map(modelMapper::toAntiaircraftResponse).collect(Collectors.toList());
    }

    @Transactional
    public AntiaircraftResponse deleteAntiaircraft(long id) {
        AntiaircraftResponse antiaircraftResponse = modelMapper.toAntiaircraftResponse(antiaircraftRepository.findById(id).orElseThrow(() -> new Error404("Antiaircraft not found")));
        antiaircraftRepository.deleteById(id);
        return antiaircraftResponse;
    }

    @Transactional
    public AntiaircraftResponse getAntiaircraft(long id) {
        return modelMapper.toAntiaircraftResponse(antiaircraftRepository.findById(id).orElseThrow(() -> new Error404("Antiaircraft not found")));
    }

    @Transactional
    public AntiaircraftResponse createAntiaircraft(AntiaircraftDto antiaircraftDto) {
        Antiaircraft antiaircraft = modelMapper.toAntiaircraft(antiaircraftDto);
        AntiaircraftType antiaircraftType = antiaircraftTypeRepository.findById(antiaircraftDto.getTypeId()).orElseThrow(() -> new Error404("AntiaircraftType not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(antiaircraftDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        antiaircraft.setFormation(militaryFormation);
        antiaircraft.setType(antiaircraftType);

        try{
            antiaircraft = antiaircraftRepository.save(antiaircraft);
            return  modelMapper.toAntiaircraftResponse(antiaircraft);
        } catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public AntiaircraftResponse updateAntiaircraft(AntiaircraftDto antiaircraftDto, long id) {
        Antiaircraft antiaircraft = antiaircraftRepository.findById(id).orElseThrow(() -> new Error404("Not found"));
        AntiaircraftType antiaircraftType = antiaircraftTypeRepository.findById(antiaircraftDto.getTypeId()).orElseThrow(() -> new Error404("Antiaircraft not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(antiaircraftDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        antiaircraft.setType(antiaircraftType);
        antiaircraft.setFormation(militaryFormation);
        antiaircraft.setRadarStatus(antiaircraftDto.getRadarStatus());
        antiaircraft.setCategory(antiaircraftDto.getCategory());
        antiaircraft.setSerialNumber(antiaircraftDto.getSerialNumber());
        antiaircraft.setYearOfManufacture(antiaircraftDto.getYearOfManufacture());

        try{
            antiaircraft = antiaircraftRepository.save(antiaircraft);
            return  modelMapper.toAntiaircraftResponse(antiaircraft);
        } catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
