package boev.app.services.interfaces;

import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface HelpService {
    List<MilitaryUnitResponse> getAll();
}
