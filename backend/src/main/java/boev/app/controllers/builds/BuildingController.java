package boev.app.controllers.builds;

import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeResponse;
import boev.app.payload.builds.building.BuildingDto;
import boev.app.payload.builds.building.BuildingResponse;
import boev.app.services.interfaces.builds.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BuildingController {
    private final BuildingService buildingService;

    @GetMapping("/getAll")
    ResponseEntity<List<BuildingResponse>> getAll(){
        return new ResponseEntity<>(buildingService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<BuildingResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(buildingService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<BuildingResponse> create(@Valid @RequestBody BuildingDto antiaircraftDto){
        return new ResponseEntity<>(buildingService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<BuildingResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(buildingService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<BuildingResponse> update(@Valid @RequestBody BuildingDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(buildingService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
