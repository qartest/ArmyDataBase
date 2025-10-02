package boev.app.repository.armaments.instatnces;

import boev.app.models.armaments.instances.fireHelp.Artillery;
import boev.app.models.armaments.instances.weapon.AutomaticWeapon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomaticWeaponRepository extends JpaRepository<AutomaticWeapon, Long> {
}
