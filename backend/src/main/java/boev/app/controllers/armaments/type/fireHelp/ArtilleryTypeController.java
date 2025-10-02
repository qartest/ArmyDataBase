package boev.app.controllers.armaments.type.fireHelp;

import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeDto;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeResponse;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeDto;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeResponse;
import boev.app.services.interfaces.armaments.type.fireHelp.ArtilleryTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/artilleryType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArtilleryTypeController {
    private final ArtilleryTypeService artilleryTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<ArtilleryTypeResponse>> getAll(){
        return new ResponseEntity<>(artilleryTypeService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<ArtilleryTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(artilleryTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<ArtilleryTypeResponse> create(@Valid @RequestBody ArtilleryTypeDto antiaircraftDto){
        return new ResponseEntity<>(artilleryTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ArtilleryTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(artilleryTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ArtilleryTypeResponse> update(@Valid @RequestBody ArtilleryTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(artilleryTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
