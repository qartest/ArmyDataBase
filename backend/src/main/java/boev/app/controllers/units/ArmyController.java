package boev.app.controllers.units;

import boev.app.payload.soldiers.solider.SoldierDto;
import boev.app.payload.soldiers.solider.SoldierResponse;
import boev.app.payload.units.army.ArmyDto;
import boev.app.payload.units.army.ArmyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.units.ArmyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/army")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ArmyController {
    private final ArmyService armyService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(armyService.getAllSimple(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<ArmyResponse>> getAll(){
        return new ResponseEntity<>(armyService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<ArmyResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(armyService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<ArmyResponse> create(@Valid @RequestBody ArmyDto antiaircraftDto){
        return new ResponseEntity<>(armyService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ArmyResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(armyService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ArmyResponse> update(@Valid @RequestBody ArmyDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(armyService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
