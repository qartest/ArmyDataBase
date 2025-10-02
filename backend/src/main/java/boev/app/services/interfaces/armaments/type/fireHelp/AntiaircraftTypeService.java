package boev.app.services.interfaces.armaments.type.fireHelp;

import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeDto;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeResponse;

import java.util.List;

public interface AntiaircraftTypeService {
    List<AntiaircraftTypeResponse> getAll();
    AntiaircraftTypeResponse get(long id);
    AntiaircraftTypeResponse delete(long id);
    AntiaircraftTypeResponse update(long id, AntiaircraftTypeDto antiaircraftTypeDto);
    AntiaircraftTypeResponse create(AntiaircraftTypeDto antiaircraftTypeDto);
}
