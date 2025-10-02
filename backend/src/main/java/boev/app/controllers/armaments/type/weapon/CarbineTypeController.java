package boev.app.controllers.armaments.type.weapon;

import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeResponse;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeResponse;
import boev.app.services.interfaces.armaments.type.weapon.CarbineTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/carbineType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CarbineTypeController {
    private final CarbineTypeService carbineTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<CarbineTypeResponse>> getAll(){
        return new ResponseEntity<>(carbineTypeService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<CarbineTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(carbineTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<CarbineTypeResponse> create(@Valid @RequestBody CarbineTypeDto antiaircraftDto){
        return new ResponseEntity<>(carbineTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CarbineTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(carbineTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CarbineTypeResponse> update(@Valid @RequestBody CarbineTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(carbineTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
