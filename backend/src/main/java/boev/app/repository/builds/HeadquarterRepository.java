package boev.app.repository.builds;

import boev.app.models.builds.Headquarter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {
}
