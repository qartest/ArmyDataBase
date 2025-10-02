package boev.app.controllers.soldiers;

import boev.app.payload.soldiers.solider.SoldierDto;
import boev.app.payload.soldiers.solider.SoldierResponse;
import boev.app.payload.soldiers.solider.SoldierSimpleDto;
import boev.app.services.interfaces.soldiers.SoldierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soldier")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SoldierController {
    private final SoldierService soldierService;

    @GetMapping("/getAll")
    ResponseEntity<List<SoldierSimpleDto>> getAll(){
        return new ResponseEntity<>(soldierService.getAll(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SoldierSimpleDto> getSimple(@PathVariable("id") long id) {
        return new ResponseEntity<>(soldierService.getSimple(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<SoldierResponse> create(@Valid @RequestBody SoldierDto antiaircraftDto){
        return new ResponseEntity<>(soldierService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<SoldierResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(soldierService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<SoldierResponse> update(@Valid @RequestBody SoldierDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(soldierService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }

}
