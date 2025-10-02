package boev.app.services.interfaces.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryDto;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryResponse;

import java.util.List;

public interface ArtilleryService {
    List<ArtilleryResponse> getAll();
    ArtilleryResponse  getArtillery(long id);
    ArtilleryResponse deleteArtillery(long id);
    ArtilleryResponse createArtillery(ArtilleryDto artilleryDto);
    ArtilleryResponse updateArtillery(long id, ArtilleryDto artilleryDto);
}
