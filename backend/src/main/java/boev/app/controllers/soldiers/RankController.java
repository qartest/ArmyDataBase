package boev.app.controllers.soldiers;

import boev.app.payload.builds.headquater.HeadquarterDto;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.soldiers.rank.RankDto;
import boev.app.payload.soldiers.rank.RankResponse;
import boev.app.services.interfaces.soldiers.RankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RankController {
    private final RankService rankService;

    @GetMapping("getAll")
    ResponseEntity<List<RankResponse>> getAll(){
        return new ResponseEntity<>(rankService.getAll(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/get/{id}")
    ResponseEntity<RankResponse> get(@PathVariable("id") long id){
        return new ResponseEntity<>(rankService.get(id), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    ResponseEntity<RankResponse> create(@Valid @RequestBody RankDto antiaircraftDto){
        return new ResponseEntity<>(rankService.create(antiaircraftDto), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<RankResponse> delete(@PathVariable ("id") long id){
        return new ResponseEntity<>(rankService.delete(id), HttpStatusCode.valueOf(200));
    }

    @PutMapping("/update/{id}")
    ResponseEntity<RankResponse> update(@Valid @RequestBody RankDto antiaircraftDto, @PathVariable ("id") long id){
        return new ResponseEntity<>(rankService.update(id, antiaircraftDto), HttpStatus.valueOf(200));
    }
}
