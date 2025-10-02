package boev.app.services.interfaces.units;

import boev.app.models.builds.Headquarter;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.payload.soldiers.solider.SoldierMinimumDto;
import boev.app.payload.units.division.DivisionDto;
import boev.app.payload.units.division.DivisionResponse;
import boev.app.payload.units.militaryFormation.EquipmentAll;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;

import java.util.List;

public interface DivisionService {
    List<MilitaryUnitResponse> getAllSimple();
    List<DivisionResponse> getAll();
    DivisionResponse get(long id);
    DivisionResponse delete(long id);
    DivisionResponse create(DivisionDto divisionDto);
    DivisionResponse update(DivisionDto divisionDto, long id);

    List<MilitaryUnitResponse> getMilitaryFormation(long id);
    List<SoldierMinimumDto> getOfficer(long id);
    List<SoldierMinimumDto> getNotOfficer(long id);

    List<HeadquarterResponse> getHeadquarter(long id);
    EquipmentAll getEquipment(long id);
    List<MilitarySpecialtyResponse> getSpecialty(long id);

    DivisionResponse MoreMilitaryFormation();
}
