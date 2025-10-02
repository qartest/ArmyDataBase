package boev.app.controllers.help;

import boev.app.payload.builds.headquater.HeadquarterResponseSimple;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.services.interfaces.HelpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HelpController {

    private final HelpService helpService;
    @GetMapping("/getAllFormations")
    ResponseEntity<List<MilitaryUnitResponse>> getAllSimple(){
        return new ResponseEntity<>(helpService.getAll(), HttpStatusCode.valueOf(200));
    }
}
