package boev.app.repository.armaments.instatnces;

import boev.app.models.armaments.instances.technique.Tank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TankRepository extends JpaRepository<Tank, Long> {
}
