package boev.app.repository.soliders;

import boev.app.models.soldiers.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
