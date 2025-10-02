package boev.app.models.soldiers.records;

import boev.app.models.soldiers.Rank;
import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.Soldier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sergeant_records")
public class SergeantRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soldier_id")
    private Soldier soldier;

    @ManyToOne
    @JoinColumn(name = "rank_id", nullable = false)
    private Rank rank; // Звание на момент создания записи

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "leadership_training_date", nullable = false)
    private LocalDate leadershipTrainingDate;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (rank == null || rank.getCategory() != RankCategory.SERGEANT) {
            throw new IllegalStateException("SergeantRecord должен ссылаться на звание категории SERGEANT");
        }
    }
}
