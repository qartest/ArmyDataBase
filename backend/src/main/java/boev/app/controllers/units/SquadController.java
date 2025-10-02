package boev.app.controllers.units;

import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.platoon.PlatoonDto;
import boev.app.payload.units.platoon.PlatoonResponse;
import boev.app.payload.units.squad.SquadDto;
import boev.app.payload.units.squad.SquadResponse;
import boev.app.services.interfaces.units.SquadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/squad")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SquadController {
    private final SquadService squadService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(squadService.getAllSimple(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/getAll")
    ResponseEntity<List<SquadResponse>> getAll(){
        return new ResponseEntity<>(squadService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<SquadResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(squadService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<SquadResponse> create(@Valid @RequestBody SquadDto antiaircraftDto){
        return new ResponseEntity<>(squadService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<SquadResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(squadService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<SquadResponse> update(@Valid @RequestBody SquadDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(squadService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
