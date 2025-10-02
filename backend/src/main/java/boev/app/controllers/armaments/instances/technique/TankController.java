package boev.app.controllers.armaments.instances.technique;

import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleDto;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleResponse;
import boev.app.payload.armaments.instances.technique.Tank.TankDto;
import boev.app.payload.armaments.instances.technique.Tank.TankResponse;
import boev.app.services.interfaces.armaments.instances.technique.TankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/tank")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TankController {
    private final TankService tankService;

    @GetMapping("/getAll")
    ResponseEntity<List<TankResponse>> getAll(){
        return new ResponseEntity<>(tankService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<TankResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(tankService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<TankResponse> create(@Valid @RequestBody TankDto antiaircraftDto){
        return new ResponseEntity<>(tankService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<TankResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(tankService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<TankResponse> update(@Valid @RequestBody TankDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(tankService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
