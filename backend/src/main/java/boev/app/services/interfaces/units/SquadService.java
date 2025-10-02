package boev.app.services.interfaces.units;

import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.squad.SquadDto;
import boev.app.payload.units.squad.SquadResponse;

import java.util.List;

public interface SquadService {
    List<MilitaryUnitResponse> getAllSimple();
    List<SquadResponse> getAll();
    SquadResponse get(long id);
    SquadResponse delete(long id);
    SquadResponse create(SquadDto squadDto);
    SquadResponse update(SquadDto squadDto, long id);
}
