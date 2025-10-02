package boev.app.controllers.armaments.instances.weapon;

import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponDto;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponResponse;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineDto;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineResponse;
import boev.app.services.interfaces.armaments.instances.weapon.CarbineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/carbinecontroller")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CarbineController {
    private final CarbineService carbineService;

    @GetMapping("/getAll")
    ResponseEntity<List<CarbineResponse>> getAll(){
        return new ResponseEntity<>(carbineService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<CarbineResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(carbineService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<CarbineResponse> create(@Valid @RequestBody CarbineDto antiaircraftDto){
        return new ResponseEntity<>(carbineService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CarbineResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(carbineService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CarbineResponse> update(@Valid @RequestBody CarbineDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(carbineService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
