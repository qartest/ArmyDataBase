package boev.app.services.interfaces.armaments.instances.weapon;

import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponDto;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponResponse;

import java.util.List;

public interface AutomaticWeaponService {
    List<AutomaticWeaponResponse> getAll();
    AutomaticWeaponResponse get(long id);
    AutomaticWeaponResponse create(AutomaticWeaponDto automaticWeaponDto);
    AutomaticWeaponResponse update(long id, AutomaticWeaponDto automaticWeaponDto);
    AutomaticWeaponResponse delete(long id);
}
