package boev.app.repository.armaments.instatnces;

import boev.app.models.armaments.instances.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
