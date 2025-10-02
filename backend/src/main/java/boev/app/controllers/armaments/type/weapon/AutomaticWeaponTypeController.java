package boev.app.controllers.armaments.type.weapon;

import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeDto;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeResponse;
import boev.app.services.interfaces.armaments.type.weapon.AutomaticWeaponTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/automaticweaponType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AutomaticWeaponTypeController {
    private final AutomaticWeaponTypeService automaticWeaponTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<AutomaticWeaponTypeResponse>> getAll(){
        return new ResponseEntity<>(automaticWeaponTypeService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<AutomaticWeaponTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(automaticWeaponTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<AutomaticWeaponTypeResponse> create(@Valid @RequestBody AutomaticWeaponTypeDto antiaircraftDto){
        return new ResponseEntity<>(automaticWeaponTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<AutomaticWeaponTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(automaticWeaponTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<AutomaticWeaponTypeResponse> update(@Valid @RequestBody AutomaticWeaponTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(automaticWeaponTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
