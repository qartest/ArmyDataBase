package boev.app.services.impl.armaments.type.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.type.fireHelp.ArtilleryType;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeDto;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeResponse;
import boev.app.repository.armaments.type.ArtilleryTypeRepository;
import boev.app.services.interfaces.armaments.type.fireHelp.ArtilleryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtilleryTypeServiceImpl implements ArtilleryTypeService {
    private final ModelMapper modelMapper;
    private final ArtilleryTypeRepository artilleryTypeRepository;

    @Transactional
    public List<ArtilleryTypeResponse> getAll() {
        return artilleryTypeRepository.findAll().stream().map(modelMapper::toArtilleryTypeResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public ArtilleryTypeResponse get(long id) {
        return modelMapper.toArtilleryTypeResponse(artilleryTypeRepository.findById(id).orElseThrow(() -> new Error404("ArtilleryType not found")));
    }

    @Transactional
    public ArtilleryTypeResponse update(long id, ArtilleryTypeDto artilleryTypeDto) {
        ArtilleryType artilleryType = artilleryTypeRepository.findById(id).orElseThrow(() -> new Error404("ArtilleryType not found"));

        artilleryType.setRange(artilleryTypeDto.getRange());
        artilleryType.setModel(artilleryTypeDto.getModel());
        artilleryType.setCaliber(artilleryTypeDto.getCaliber());
        artilleryType.setRateOfFire(artilleryTypeDto.getRateOfFire());

        try{
            artilleryType = artilleryTypeRepository.save(artilleryType);
            return modelMapper.toArtilleryTypeResponse(artilleryType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public ArtilleryTypeResponse create(ArtilleryTypeDto artilleryTypeDto) {
        ArtilleryType artilleryType = modelMapper.toArtilleryType(artilleryTypeDto);

        try{
            artilleryType = artilleryTypeRepository.save(artilleryType);
            return modelMapper.toArtilleryTypeResponse(artilleryType);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    @Transactional
    public ArtilleryTypeResponse delete(long id) {
        ArtilleryTypeResponse artilleryTypeResponse = modelMapper.toArtilleryTypeResponse(artilleryTypeRepository.findById(id).orElseThrow(() -> new Error404("ArtilleryType not found")));
        artilleryTypeRepository.deleteById(id);
        return artilleryTypeResponse;
    }
}
