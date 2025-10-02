package boev.app.controllers.units;

import boev.app.payload.units.militaryFormation.MilitaryFormationDto;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.platoon.PlatoonDto;
import boev.app.payload.units.platoon.PlatoonResponse;
import boev.app.services.interfaces.units.PlatoonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/platoon")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlatoonController {
    private final PlatoonService platoonService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(platoonService.getAllSimple(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/getAll")
    ResponseEntity<List<PlatoonResponse>> getAll(){
        return new ResponseEntity<>(platoonService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<PlatoonResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(platoonService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<PlatoonResponse> create(@Valid @RequestBody PlatoonDto antiaircraftDto){
        return new ResponseEntity<>(platoonService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<PlatoonResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(platoonService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<PlatoonResponse> update(@Valid @RequestBody PlatoonDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(platoonService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
