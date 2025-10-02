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
@Table(name = "private_records")
public class PrivateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soldier_id")
    private Soldier soldier;

    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (rank == null || rank.getCategory() != RankCategory.PRIVATE) {
            throw new IllegalStateException("PrivateRecord должен ссылаться на звание категории PRIVATE");
        }
    }
}
