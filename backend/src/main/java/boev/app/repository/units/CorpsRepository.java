package boev.app.repository.units;

import boev.app.models.units.Corp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpsRepository extends JpaRepository<Corp, Long> {
}
