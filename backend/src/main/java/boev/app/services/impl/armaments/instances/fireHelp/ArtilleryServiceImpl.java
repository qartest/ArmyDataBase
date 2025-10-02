package boev.app.services.impl.armaments.instances.fireHelp;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.instances.fireHelp.Artillery;
import boev.app.models.armaments.type.fireHelp.ArtilleryType;
import boev.app.models.units.MilitaryFormation;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryDto;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryResponse;
import boev.app.repository.armaments.instatnces.ArtilleryRepository;
import boev.app.repository.armaments.type.ArtilleryTypeRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.services.interfaces.armaments.instances.fireHelp.ArtilleryService;
import boev.app.services.interfaces.armaments.type.fireHelp.ArtilleryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArtilleryServiceImpl implements ArtilleryService {
    private final ModelMapper modelMapper;
    private final ArtilleryRepository artilleryRepository;
    private final ArtilleryTypeRepository artilleryTypeRepository;
    private final MilitaryFormationRepository militaryFormationRepository;


    @Transactional
    public List<ArtilleryResponse> getAll() {
        return artilleryRepository.findAll().stream().map(modelMapper::toArtilleryResponse).collect(Collectors.toList());
    }

    @Transactional
    public ArtilleryResponse getArtillery(long id) {
        return modelMapper.toArtilleryResponse(artilleryRepository.findById(id).orElseThrow(() -> new Error404("Artillery not found")));
    }

    @Transactional
    public ArtilleryResponse deleteArtillery(long id) {
        ArtilleryResponse artilleryResponse = modelMapper.toArtilleryResponse(artilleryRepository.findById(id).orElseThrow(() -> new Error404("Artillery not found")));
        artilleryRepository.deleteById(id);
        return artilleryResponse;
    }

    @Transactional
    public ArtilleryResponse createArtillery(ArtilleryDto artilleryDto) {
        Artillery artillery = modelMapper.toArtillery(artilleryDto);
        ArtilleryType artilleryType = artilleryTypeRepository.findById(artilleryDto.getTypeId()).orElseThrow(() -> new Error404("ArtilleryType not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(artilleryDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        artillery.setFormation(militaryFormation);
        artillery.setType(artilleryType);

        try{
            artillery = artilleryRepository.save(artillery);
            return modelMapper.toArtilleryResponse(artillery);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public ArtilleryResponse updateArtillery(long id, ArtilleryDto artilleryDto) {
        Artillery artillery = artilleryRepository.findById(id).orElseThrow(() -> new Error404("Artillery not found"));
        ArtilleryType artilleryType = artilleryTypeRepository.findById(artilleryDto.getTypeId()).orElseThrow(() -> new Error404("ArtilleryType not found"));
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(artilleryDto.getFormationId()).orElseThrow(() -> new Error404("Formation not found"));

        artillery.setType(artilleryType);
        artillery.setFormation(militaryFormation);
        artillery.setCategory(artilleryDto.getCategory());
        artillery.setSerialNumber(artilleryDto.getSerialNumber());
        artillery.setBarrelStatus(artilleryDto.getBarrelStatus());
        artillery.setYearOfManufacture(artilleryDto.getYearOfManufacture());
        artillery.setShots(artilleryDto.getShots());

        try{
            artillery = artilleryRepository.save(artillery);
            return modelMapper.toArtilleryResponse(artillery);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
