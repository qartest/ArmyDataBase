package boev.app.services.interfaces.armaments.type.weapon;

import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeResponse;

import java.util.List;

public interface CarbineTypeService {
    List<CarbineTypeResponse> getAll();
    CarbineTypeResponse get(long id);
    CarbineTypeResponse delete(long id);
    CarbineTypeResponse update(long id, CarbineTypeDto carbineTypeDto);
    CarbineTypeResponse create(CarbineTypeDto carbineTypeDto);
}
