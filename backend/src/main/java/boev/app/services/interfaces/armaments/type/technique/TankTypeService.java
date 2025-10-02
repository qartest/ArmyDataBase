package boev.app.services.interfaces.armaments.type.technique;

import boev.app.payload.armaments.type.technique.Tank.TankTypeDto;
import boev.app.payload.armaments.type.technique.Tank.TankTypeResponse;

import java.util.List;

public interface TankTypeService {
    List<TankTypeResponse> getAll();
    TankTypeResponse get(long id);
    TankTypeResponse delete(long id);
    TankTypeResponse create(TankTypeDto tankTypeDto);
    TankTypeResponse update(long id, TankTypeDto tankTypeDto);

}
