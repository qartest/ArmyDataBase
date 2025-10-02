package boev.app.services.interfaces.units;

import boev.app.payload.units.army.ArmyDto;
import boev.app.payload.units.army.ArmyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface ArmyService {
    List<MilitaryUnitResponse> getAllSimple();
    List<ArmyResponse> getAll();
    ArmyResponse get(long id);
    ArmyResponse delete(long id);
    ArmyResponse create(ArmyDto armyDto);
    ArmyResponse update(ArmyDto armyDto, long id);
}
