package boev.app.controllers.builds;

import boev.app.payload.builds.building.BuildingDto;
import boev.app.payload.builds.building.BuildingResponse;
import boev.app.payload.builds.headquater.HeadquarterDto;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.builds.headquater.HeadquarterResponseSimple;
import boev.app.services.interfaces.builds.HeadquarterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/headquarter")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HeadquarterController {
    private final HeadquarterService headquarterService;

    @GetMapping("/getAllSimple")
    ResponseEntity<List<HeadquarterResponseSimple>> getAllSimple(){
        return new ResponseEntity<>(headquarterService.getAllSimple(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/getAll")
    ResponseEntity<List<HeadquarterResponse>> getAll(){
        return new ResponseEntity<>(headquarterService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<HeadquarterResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(headquarterService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<HeadquarterResponse> create(@Valid @RequestBody HeadquarterDto antiaircraftDto){
        return new ResponseEntity<>(headquarterService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<HeadquarterResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(headquarterService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<HeadquarterResponse> update(@Valid @RequestBody HeadquarterDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(headquarterService.update(antiaircraftDto, id), HttpStatus.valueOf(200));
    }

}
