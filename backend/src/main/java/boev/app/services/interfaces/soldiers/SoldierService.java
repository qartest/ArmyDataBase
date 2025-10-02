package boev.app.services.interfaces.soldiers;

import boev.app.payload.soldiers.solider.SoldierDto;
import boev.app.payload.soldiers.solider.SoldierResponse;
import boev.app.payload.soldiers.solider.SoldierSimpleDto;

import java.util.List;

public interface SoldierService {
    SoldierSimpleDto getSimple(long id);
//    List<SoldierSimpleDto> getSimpleByIds();
    List<SoldierSimpleDto> getAll();
    SoldierResponse get(long id);
    SoldierResponse delete(long id);
    SoldierResponse update(long id, SoldierDto soldierDto);
    SoldierResponse create(SoldierDto soldierDto);
}
