package boev.app.controllers.armaments.type.fireHelp;


import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeDto;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeResponse;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeDto;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeResponse;
import boev.app.services.interfaces.armaments.type.fireHelp.MissileWeaponTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/missileweaponType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MissileWeaponTypeController {
    private final MissileWeaponTypeService missileWeaponTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<MissileWeaponTypeResponse>> getAll(){
        return new ResponseEntity<>(missileWeaponTypeService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<MissileWeaponTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(missileWeaponTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<MissileWeaponTypeResponse> create(@Valid @RequestBody MissileWeaponTypeDto antiaircraftDto){
        return new ResponseEntity<>(missileWeaponTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<MissileWeaponTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(missileWeaponTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<MissileWeaponTypeResponse> update(@Valid @RequestBody MissileWeaponTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(missileWeaponTypeService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
