package boev.app.services.interfaces.armaments.type.fireHelp;

import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeDto;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeResponse;

import java.util.List;

public interface ArtilleryTypeService {
    List<ArtilleryTypeResponse> getAll();
    ArtilleryTypeResponse get(long id);
    ArtilleryTypeResponse update(long id, ArtilleryTypeDto artilleryTypeDto);
    ArtilleryTypeResponse create(ArtilleryTypeDto artilleryTypeDto);
    ArtilleryTypeResponse delete(long id);
}
