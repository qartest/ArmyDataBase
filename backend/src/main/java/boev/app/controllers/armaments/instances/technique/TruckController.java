package boev.app.controllers.armaments.instances.technique;

import boev.app.payload.armaments.instances.technique.Tank.TankDto;
import boev.app.payload.armaments.instances.technique.Tank.TankResponse;
import boev.app.payload.armaments.instances.technique.Truck.TruckDto;
import boev.app.payload.armaments.instances.technique.Truck.TruckResponse;
import boev.app.services.interfaces.armaments.instances.technique.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/truck")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TruckController {
    private final TruckService truckService;

    @GetMapping("/getAll")
    ResponseEntity<List<TruckResponse>> getAll(){
        return new ResponseEntity<>(truckService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<TruckResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(truckService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<TruckResponse> create(@Valid @RequestBody TruckDto antiaircraftDto){
        return new ResponseEntity<>(truckService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<TruckResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(truckService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<TruckResponse> update(@Valid @RequestBody TruckDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(truckService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
