package boev.app.repository.soliders;

import boev.app.models.soldiers.specialty.MilitarySpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilitarySpeciallyEntityRepository extends JpaRepository<MilitarySpecialtyEntity, Long> {
}
