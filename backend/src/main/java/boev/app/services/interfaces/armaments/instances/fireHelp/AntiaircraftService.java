package boev.app.services.interfaces.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftDto;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;

import java.util.List;

public interface AntiaircraftService {
    List<AntiaircraftResponse> getAll();
    AntiaircraftResponse deleteAntiaircraft(long id);
    AntiaircraftResponse getAntiaircraft(long id);
    AntiaircraftResponse createAntiaircraft(AntiaircraftDto antiaircraftDto);
    AntiaircraftResponse updateAntiaircraft(AntiaircraftDto antiaircraftDto, long id);
}
