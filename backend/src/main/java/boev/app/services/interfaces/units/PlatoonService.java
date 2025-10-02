package boev.app.services.interfaces.units;

import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.platoon.PlatoonDto;
import boev.app.payload.units.platoon.PlatoonResponse;

import java.util.List;

public interface PlatoonService {
    List<MilitaryUnitResponse> getAllSimple();
    List<PlatoonResponse> getAll();
    PlatoonResponse get(long id);
    PlatoonResponse delete(long id);
    PlatoonResponse create(PlatoonDto platoonDto);
    PlatoonResponse update(PlatoonDto platoonDto, long id);
}
