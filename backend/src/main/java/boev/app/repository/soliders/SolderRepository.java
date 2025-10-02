
package boev.app.repository.soliders;

import boev.app.models.soldiers.Soldier;
import boev.app.payload.soldiers.solider.SoldierSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface SolderRepository extends JpaRepository<Soldier, Long> {
    @Query("SELECT s FROM Soldier s " +
            "LEFT JOIN FETCH s.rank " +
            "LEFT JOIN FETCH s.squad " +
            "LEFT JOIN FETCH s.commandedUnit " +
            "LEFT JOIN FETCH s.specialties " +
            "LEFT JOIN FETCH s.privateRecords " +
            "LEFT JOIN FETCH s.sergeantRecords " +
            "LEFT JOIN FETCH s.officerRecords " +
            "WHERE s.id = :id")
    Optional<Soldier> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT s FROM Soldier s " +
            "LEFT JOIN FETCH s.rank " +
            "LEFT JOIN FETCH s.squad " +
            "LEFT JOIN FETCH s.commandedUnit " +
            "LEFT JOIN FETCH s.specialties " +
            "LEFT JOIN FETCH s.privateRecords " +
            "LEFT JOIN FETCH s.sergeantRecords " +
            "LEFT JOIN FETCH s.officerRecords " +
            "WHERE s.id IN :ids")
    List<Soldier> findByIdsWithDetails(@Param("ids") List<Long> ids);
}