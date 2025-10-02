package boev.app.services.interfaces.armaments.type.technique;

import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;

import java.util.List;

public interface TruckTypeService {
    List<TruckTypeResponse> getAll();
    TruckTypeResponse get(long id);
    TruckTypeResponse delete(long id);
    TruckTypeResponse update(long id, TruckTypeDto truckTypeDto);
    TruckTypeResponse create(TruckTypeDto truckTypeDto);
}
