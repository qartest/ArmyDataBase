package boev.app.services.interfaces.soldiers;

import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyDto;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;

import java.util.List;

public interface SpecialtyService {
    List<MilitarySpecialtyResponse> getAll();
    MilitarySpecialtyResponse delete(long id);
    MilitarySpecialtyResponse create(MilitarySpecialtyDto militarySpecialtyDto);
}
