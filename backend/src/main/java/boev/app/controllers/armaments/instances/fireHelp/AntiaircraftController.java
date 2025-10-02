package boev.app.controllers.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftDto;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;
import boev.app.services.interfaces.armaments.instances.fireHelp.AntiaircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment/antiaircraft")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AntiaircraftController {
    private final AntiaircraftService antiaircraftService;

    @GetMapping("/getAll")
    ResponseEntity<List<AntiaircraftResponse>> getAll(){
        return new ResponseEntity<>(antiaircraftService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<AntiaircraftResponse> get(@PathVariable ("id") long id){
        return new ResponseEntity<>(antiaircraftService.getAntiaircraft(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<AntiaircraftResponse> create(@Valid @RequestBody AntiaircraftDto antiaircraftDto){
        return new ResponseEntity<>(antiaircraftService.createAntiaircraft(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<AntiaircraftResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(antiaircraftService.deleteAntiaircraft(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<AntiaircraftResponse> update(@Valid @RequestBody AntiaircraftDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(antiaircraftService.updateAntiaircraft(antiaircraftDto, id), HttpStatus.valueOf(200));
    }


    private void Test(){
        int a = 1;
        long b = a;

    }
}
