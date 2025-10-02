package boev.app.services.impl;

import boev.app.models.ModelMapper;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.services.interfaces.HelpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HelpServiceImpl implements HelpService {
    private final ModelMapper modelMapper;
    private final MilitaryUnitRepository militaryUnitRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAll() {
        return militaryUnitRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }
}
