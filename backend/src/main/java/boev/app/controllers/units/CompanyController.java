package boev.app.controllers.units;

import boev.app.payload.units.army.ArmyDto;
import boev.app.payload.units.army.ArmyResponse;
import boev.app.payload.units.company.CompanyDto;
import boev.app.payload.units.company.CompanyResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.units.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units/company")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/getAll/Simple")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(companyService.getAllSimple(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<CompanyResponse>> getAll(){
        return new ResponseEntity<>(companyService.getAll(), HttpStatusCode.valueOf(200));
    }


    @GetMapping("/get/{id}")
    ResponseEntity<CompanyResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(companyService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyDto antiaircraftDto){
        return new ResponseEntity<>(companyService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CompanyResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(companyService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<CompanyResponse> update(@Valid @RequestBody CompanyDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(companyService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }
}
