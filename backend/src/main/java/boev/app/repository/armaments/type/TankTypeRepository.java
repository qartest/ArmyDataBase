package boev.app.repository.armaments.type;

import boev.app.models.armaments.type.technique.TankType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TankTypeRepository extends JpaRepository<TankType, Long> {
}
