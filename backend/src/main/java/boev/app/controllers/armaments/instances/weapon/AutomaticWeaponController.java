package boev.app.controllers.armaments.instances.weapon;


import boev.app.payload.armaments.instances.technique.Truck.TruckDto;
import boev.app.payload.armaments.instances.technique.Truck.TruckResponse;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponDto;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponResponse;
import boev.app.services.interfaces.armaments.instances.weapon.AutomaticWeaponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/automaticweapon")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AutomaticWeaponController {
    private final AutomaticWeaponService automaticWeaponService;

    @GetMapping("/getAll")
    ResponseEntity<List<AutomaticWeaponResponse>> getAll(){
        return new ResponseEntity<>(automaticWeaponService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<AutomaticWeaponResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(automaticWeaponService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<AutomaticWeaponResponse> create(@Valid @RequestBody AutomaticWeaponDto antiaircraftDto){
        return new ResponseEntity<>(automaticWeaponService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<AutomaticWeaponResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(automaticWeaponService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<AutomaticWeaponResponse> update(@Valid @RequestBody AutomaticWeaponDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(automaticWeaponService.update( id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
