package boev.app.controllers.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftDto;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryDto;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryResponse;
import boev.app.services.interfaces.armaments.instances.fireHelp.ArtilleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/artillery")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArtilleryController {
    private final ArtilleryService artilleryService;

    @GetMapping("/getAll")
    ResponseEntity<List<ArtilleryResponse>> getAll(){
        return new ResponseEntity<>(artilleryService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<ArtilleryResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(artilleryService.getArtillery(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<ArtilleryResponse> create(@Valid @RequestBody ArtilleryDto antiaircraftDto){
        return new ResponseEntity<>(artilleryService.createArtillery(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ArtilleryResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(artilleryService.deleteArtillery(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ArtilleryResponse> update(@Valid @RequestBody ArtilleryDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(artilleryService.updateArtillery(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
