package boev.app.controllers.units;

import boev.app.payload.units.company.CompanyDto;
import boev.app.payload.units.company.CompanyResponse;
import boev.app.payload.units.corp.CorpDto;
import boev.app.payload.units.corp.CorpResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.units.CorpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/corp")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CorpController {
    private final CorpService corpService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(corpService.getAllSimple(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<CorpResponse>> getAll(){
        return new ResponseEntity<>(corpService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    ResponseEntity<CorpResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(corpService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<CorpResponse> create(@Valid @RequestBody CorpDto antiaircraftDto){
        return new ResponseEntity<>(corpService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CorpResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(corpService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CorpResponse> update(@Valid @RequestBody CorpDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(corpService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
