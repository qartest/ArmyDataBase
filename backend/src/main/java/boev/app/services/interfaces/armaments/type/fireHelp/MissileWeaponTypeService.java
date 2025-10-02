package boev.app.services.interfaces.armaments.type.fireHelp;

import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeDto;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeResponse;

import java.util.List;

public interface MissileWeaponTypeService {
    List<MissileWeaponTypeResponse> getAll();
    MissileWeaponTypeResponse get(long id);
    MissileWeaponTypeResponse update(MissileWeaponTypeDto missileWeaponTypeDto, long id);
    MissileWeaponTypeResponse create(MissileWeaponTypeDto missileWeaponTypeDto);
    MissileWeaponTypeResponse delete(long id);
}
