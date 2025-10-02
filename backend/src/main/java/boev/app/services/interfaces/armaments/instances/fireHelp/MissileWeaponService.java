package boev.app.services.interfaces.armaments.instances.fireHelp;

import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponDto;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponResponse;

import java.util.List;

public interface MissileWeaponService {
    List<MissileWeaponResponse> getAll();
    MissileWeaponResponse createMissileWeapon(MissileWeaponDto missileWeaponDto);
    MissileWeaponResponse deleteMissileWeapon(long id);
    MissileWeaponResponse updateMissileWeapon(MissileWeaponDto missileWeaponDto, long id);
    MissileWeaponResponse getMissileWeapon(Long id);
}
