package boev.app.controllers.units;

import boev.app.payload.units.corp.CorpDto;
import boev.app.payload.units.corp.CorpResponse;
import boev.app.payload.units.division.DivisionDto;
import boev.app.payload.units.division.DivisionResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.units.DivisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/division")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DivisionController {
    private final DivisionService divisionService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(divisionService.getAllSimple(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<DivisionResponse>> getAll(){
        return new ResponseEntity<>(divisionService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<DivisionResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(divisionService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<DivisionResponse> create(@Valid @RequestBody DivisionDto antiaircraftDto){
        return new ResponseEntity<>(divisionService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<DivisionResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(divisionService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<DivisionResponse> update(@Valid @RequestBody DivisionDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(divisionService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
