package boev.app.services.interfaces.armaments.instances.technique;

import boev.app.payload.armaments.instances.technique.Tank.TankDto;
import boev.app.payload.armaments.instances.technique.Tank.TankResponse;

import java.util.List;

public interface TankService {
    List<TankResponse> getAll();
    TankResponse get(long id);
    TankResponse create(TankDto tankDto);
    TankResponse delete(long id);
    TankResponse update(TankDto tankDto, long id);
}
