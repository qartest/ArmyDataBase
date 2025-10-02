package boev.app.repository.armaments.instatnces;

import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.instances.fireHelp.Artillery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtilleryRepository extends JpaRepository<Artillery, Long> {
}
