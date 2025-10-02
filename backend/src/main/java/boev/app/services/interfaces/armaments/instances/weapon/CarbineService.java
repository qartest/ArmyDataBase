package boev.app.services.interfaces.armaments.instances.weapon;

import boev.app.payload.armaments.instances.weapon.Carbine.CarbineDto;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineResponse;

import java.util.List;

public interface CarbineService {
    List<CarbineResponse> getAll();
    CarbineResponse get(long id);
    CarbineResponse delete(long id);
    CarbineResponse update(CarbineDto carbineDto, long id);
    CarbineResponse create(CarbineDto carbineDto);
}
