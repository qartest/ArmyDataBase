package boev.app.services.interfaces.armaments.instances.technique;

import boev.app.payload.armaments.instances.technique.Truck.TruckDto;
import boev.app.payload.armaments.instances.technique.Truck.TruckResponse;

import java.util.List;

public interface TruckService {
    List<TruckResponse> getAll();
    TruckResponse get(long id);
    TruckResponse update(TruckDto truckDto, long id);
    TruckResponse delete(long id);
    TruckResponse create(TruckDto truckDto);
}
