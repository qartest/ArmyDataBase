package boev.app.controllers.armaments.type.technique;

import boev.app.payload.armaments.type.technique.Tank.TankTypeDto;
import boev.app.payload.armaments.type.technique.Tank.TankTypeResponse;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;
import boev.app.services.interfaces.armaments.type.technique.TruckTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/truckType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TruckTypeController {
    private final TruckTypeService truckTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<TruckTypeResponse>> getAll(){
        return new ResponseEntity<>(truckTypeService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<TruckTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(truckTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<TruckTypeResponse> create(@Valid @RequestBody TruckTypeDto antiaircraftDto){
        return new ResponseEntity<>(truckTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<TruckTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(truckTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<TruckTypeResponse> update(@Valid @RequestBody TruckTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(truckTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
