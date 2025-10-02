package boev.app.services.interfaces.builds;

import boev.app.payload.builds.headquater.HeadquarterDto;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.builds.headquater.HeadquarterResponseSimple;

import java.util.List;

public interface HeadquarterService {
    List<HeadquarterResponseSimple> getAllSimple();
    List<HeadquarterResponse> getAll();
    HeadquarterResponse get(long id);
    HeadquarterResponse create(HeadquarterDto headquarterDto);
    HeadquarterResponse delete(long id);
    HeadquarterResponse update(HeadquarterDto headquarterDto, long id);
}
