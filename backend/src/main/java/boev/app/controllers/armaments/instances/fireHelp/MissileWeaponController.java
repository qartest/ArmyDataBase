package boev.app.controllers.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryDto;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryResponse;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponDto;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponResponse;
import boev.app.services.interfaces.armaments.instances.fireHelp.ArtilleryService;
import boev.app.services.interfaces.armaments.instances.fireHelp.MissileWeaponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/missileweapon")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MissileWeaponController {
    private final MissileWeaponService missileWeaponService;

    @GetMapping("/getAll")
    ResponseEntity<List<MissileWeaponResponse>> getAll(){
        return new ResponseEntity<>(missileWeaponService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<MissileWeaponResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(missileWeaponService.getMissileWeapon(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<MissileWeaponResponse> create(@Valid @RequestBody MissileWeaponDto antiaircraftDto){
        return new ResponseEntity<>(missileWeaponService.createMissileWeapon(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<MissileWeaponResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(missileWeaponService.deleteMissileWeapon(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<MissileWeaponResponse> update(@Valid @RequestBody MissileWeaponDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(missileWeaponService.updateMissileWeapon(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
