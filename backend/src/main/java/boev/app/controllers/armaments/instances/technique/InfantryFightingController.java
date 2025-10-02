package boev.app.controllers.armaments.instances.technique;

import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponDto;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponResponse;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleDto;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleResponse;
import boev.app.services.interfaces.armaments.instances.technique.InfantryFightingVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/IFV")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InfantryFightingController {
    private final InfantryFightingVehicleService infantryFightingVehicleService;

    @GetMapping("/getAll")
    ResponseEntity<List<InfantryFightingVehicleResponse>> getAll(){
        return new ResponseEntity<>(infantryFightingVehicleService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<InfantryFightingVehicleResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<InfantryFightingVehicleResponse> create(@Valid @RequestBody InfantryFightingVehicleDto antiaircraftDto){
        return new ResponseEntity<>(infantryFightingVehicleService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<InfantryFightingVehicleResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<InfantryFightingVehicleResponse> update(@Valid @RequestBody InfantryFightingVehicleDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(infantryFightingVehicleService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
