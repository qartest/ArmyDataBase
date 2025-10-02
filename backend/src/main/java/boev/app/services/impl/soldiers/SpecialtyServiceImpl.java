package boev.app.services.impl.soldiers;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.specialty.MilitarySpecialtyEntity;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyDto;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.repository.soliders.MilitarySpeciallyEntityRepository;
import boev.app.services.interfaces.soldiers.SpecialtyService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final MilitarySpeciallyEntityRepository militarySpeciallyEntityRepository;
    private final ModelMapper modelMapper;
    @Transactional
    public List<MilitarySpecialtyResponse> getAll() {
        return militarySpeciallyEntityRepository.findAll().stream().map(modelMapper::toMilitarySpecialtyResponse).collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public MilitarySpecialtyResponse delete(long id) {
        MilitarySpecialtyResponse militarySpecialtyResponse = modelMapper.toMilitarySpecialtyResponse(militarySpeciallyEntityRepository.findById(id).orElseThrow(() -> new Error404("Speal not found")));
        militarySpeciallyEntityRepository.deleteById(id);
        return militarySpecialtyResponse;
    }

    @Transactional
    public MilitarySpecialtyResponse create(MilitarySpecialtyDto militarySpecialtyDto) {
        MilitarySpecialtyEntity militarySpecialtyEntity = modelMapper.toMilitarySpecialtyEntity(militarySpecialtyDto);

        try{
            militarySpecialtyEntity = militarySpeciallyEntityRepository.save(militarySpecialtyEntity);
            return modelMapper.toMilitarySpecialtyResponse(militarySpecialtyEntity);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
