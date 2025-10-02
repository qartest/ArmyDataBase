package boev.app.controllers.armaments.type.technique;

import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeDto;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeResponse;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeDto;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeResponse;
import boev.app.services.interfaces.armaments.type.technique.InfantryFightingVehicleTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/IFVType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InfantryFightingVehicleTypeController {
    private final InfantryFightingVehicleTypeService infantryFightingVehicleTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<InfantryFightingVehicleTypeResponse>> getAll(){
        return new ResponseEntity<>(infantryFightingVehicleTypeService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<InfantryFightingVehicleTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<InfantryFightingVehicleTypeResponse> create(@Valid @RequestBody InfantryFightingVehicleTypeDto antiaircraftDto){
        return new ResponseEntity<>(infantryFightingVehicleTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<InfantryFightingVehicleTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<InfantryFightingVehicleTypeResponse> update(@Valid @RequestBody InfantryFightingVehicleTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
