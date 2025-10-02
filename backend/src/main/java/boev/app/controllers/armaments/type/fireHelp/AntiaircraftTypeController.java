package boev.app.controllers.armaments.type.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineDto;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineResponse;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeDto;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeResponse;
import boev.app.services.interfaces.armaments.type.fireHelp.AntiaircraftTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/antiaircraftType")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AntiaircraftTypeController {
    private final AntiaircraftTypeService antiaircraftTypeService;

    @GetMapping("/getAll")
    ResponseEntity<List<AntiaircraftTypeResponse>> getAll(){
        return new ResponseEntity<>(antiaircraftTypeService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<AntiaircraftTypeResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(antiaircraftTypeService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<AntiaircraftTypeResponse> create(@Valid @RequestBody AntiaircraftTypeDto antiaircraftDto){
        return new ResponseEntity<>(antiaircraftTypeService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<AntiaircraftTypeResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(antiaircraftTypeService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<AntiaircraftTypeResponse> update(@Valid @RequestBody AntiaircraftTypeDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(antiaircraftTypeService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
