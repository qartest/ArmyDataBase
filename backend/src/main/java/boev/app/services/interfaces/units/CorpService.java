package boev.app.services.interfaces.units;

import boev.app.payload.units.corp.CorpDto;
import boev.app.payload.units.corp.CorpResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface CorpService {
    List<MilitaryUnitResponse> getAllSimple();
    List<CorpResponse> getAll();
    CorpResponse get(long id);
    CorpResponse delete(long id);
    CorpResponse update(CorpDto corpDto, long id);
    CorpResponse create(CorpDto corpDto);
}
