package boev.app.services.interfaces.builds;

import boev.app.payload.builds.building.BuildingDto;
import boev.app.payload.builds.building.BuildingResponse;

import java.util.List;

public interface BuildingService {
    List<BuildingResponse> getAll();
    BuildingResponse get(long id);
    BuildingResponse update(long id, BuildingDto buildingDto);
    BuildingResponse create(BuildingDto buildingDto);
    BuildingResponse delete(long id);
}
