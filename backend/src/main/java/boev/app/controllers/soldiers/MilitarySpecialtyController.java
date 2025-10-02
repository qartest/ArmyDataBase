package boev.app.controllers.soldiers;

import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyDto;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.payload.soldiers.rank.RankDto;
import boev.app.payload.soldiers.rank.RankResponse;
import boev.app.services.interfaces.soldiers.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialty")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MilitarySpecialtyController {
    private final SpecialtyService specialtyService;

    @GetMapping("getAll")
    ResponseEntity<List<MilitarySpecialtyResponse>> getAll(){
        return new ResponseEntity<>(specialtyService.getAll(), HttpStatusCode.valueOf(200));
    }


    @PostMapping("/create")
    ResponseEntity<MilitarySpecialtyResponse> create(@Valid @RequestBody MilitarySpecialtyDto antiaircraftDto){
        return new ResponseEntity<>(specialtyService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<MilitarySpecialtyResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(specialtyService.delete(id), HttpStatusCode.valueOf(200));
    }


}
