package boev.app.services.interfaces.units;

import boev.app.payload.units.militaryFormation.MilitaryFormationDto;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponse;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponseLight;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface MilitaryFormationService {
    List<MilitaryUnitResponse> getAllSimple();
    List<MilitaryFormationResponseLight> getAll();
    MilitaryUnitResponse delete(long id);
    MilitaryUnitResponse create(MilitaryFormationDto militaryFormationDto);
    MilitaryUnitResponse update(MilitaryFormationDto militaryFormationDto, long id);
}
