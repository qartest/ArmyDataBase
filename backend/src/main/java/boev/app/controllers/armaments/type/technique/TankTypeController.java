package boev.app.controllers.armaments.type.technique;

import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeDto;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeResponse;
import boev.app.payload.armaments.type.technique.Tank.TankTypeDto;
import boev.app.payload.armaments.type.technique.Tank.TankTypeResponse;
import boev.app.services.interfaces.armaments.type.technique.TankTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/tankType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TankTypeController {
    private final TankTypeService tankTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<TankTypeResponse>> getAll(){
        return new ResponseEntity<>(tankTypeService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<TankTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(tankTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<TankTypeResponse> create(@Valid @RequestBody TankTypeDto antiaircraftDto){
        return new ResponseEntity<>(tankTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<TankTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(tankTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<TankTypeResponse> update(@Valid @RequestBody TankTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(tankTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
