package boev.app.controllers.units;

import boev.app.payload.units.division.DivisionDto;
import boev.app.payload.units.division.DivisionResponse;
import boev.app.payload.units.militaryFormation.MilitaryFormationDto;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponse;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponseLight;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.units.MilitaryFormationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/militaryFormation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MilitaryFormationController {
    private final MilitaryFormationService militaryFormationService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(militaryFormationService.getAllSimple(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/getAll")
    ResponseEntity<List<MilitaryFormationResponseLight>> getAll(){
        return new ResponseEntity<>(militaryFormationService.getAll(), HttpStatusCode.valueOf(200));
    }


//    @GetMapping("/get/{id}")
//    ResponseEntity<MilitaryFormationResponse> get(@PathVariable("id") long id){
//        return new ResponseEntity<>(militaryFormationService.get(id), HttpStatusCode.valueOf(200));
//    }

    @PostMapping("/create")
    ResponseEntity<MilitaryUnitResponse> create(@Valid @RequestBody MilitaryFormationDto antiaircraftDto){
        return new ResponseEntity<>(militaryFormationService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<MilitaryUnitResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(militaryFormationService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<MilitaryUnitResponse> update(@Valid @RequestBody MilitaryFormationDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(militaryFormationService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
