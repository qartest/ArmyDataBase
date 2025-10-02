package boev.app.services.interfaces.armaments.type.technique;

import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeDto;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeResponse;
import boev.app.repository.armaments.type.InfantryFightingVehicleTypeRepository;

import java.util.List;

public interface InfantryFightingVehicleTypeService {
    List<InfantryFightingVehicleTypeResponse> getAll();
    InfantryFightingVehicleTypeResponse get(long id);
    InfantryFightingVehicleTypeResponse delete(long id);
    InfantryFightingVehicleTypeResponse update(long id, InfantryFightingVehicleTypeDto infantryFightingVehicleTypeDto);
    InfantryFightingVehicleTypeResponse create(InfantryFightingVehicleTypeDto infantryFightingVehicleTypeDto);
}
