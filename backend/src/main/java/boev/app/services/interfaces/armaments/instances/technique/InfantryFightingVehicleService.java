package boev.app.services.interfaces.armaments.instances.technique;

import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleDto;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleResponse;

import java.util.List;

public interface InfantryFightingVehicleService {
    List<InfantryFightingVehicleResponse> getAll();
    InfantryFightingVehicleResponse get(long id);
    InfantryFightingVehicleResponse delete(long id);
    InfantryFightingVehicleResponse create(InfantryFightingVehicleDto infantryFightingVehicleDto);
    InfantryFightingVehicleResponse update(long id, InfantryFightingVehicleDto infantryFightingVehicleDto);
}
