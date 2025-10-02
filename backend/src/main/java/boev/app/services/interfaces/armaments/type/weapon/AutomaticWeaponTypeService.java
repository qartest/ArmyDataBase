package boev.app.services.interfaces.armaments.type.weapon;

import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeDto;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeResponse;

import java.util.List;

public interface AutomaticWeaponTypeService {
    List<AutomaticWeaponTypeResponse> getAll();
    AutomaticWeaponTypeResponse get(long id);
    AutomaticWeaponTypeResponse delete(long id);
    AutomaticWeaponTypeResponse update(long id, AutomaticWeaponTypeDto automaticWeaponTypeDto);
    AutomaticWeaponTypeResponse create(AutomaticWeaponTypeDto automaticWeaponTypeDto);
}
