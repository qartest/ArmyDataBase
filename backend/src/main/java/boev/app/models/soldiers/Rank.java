package boev.app.models.soldiers;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ranks")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private RussianArmyRank name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private RankCategory category;

    @OneToMany(mappedBy = "rank", fetch = FetchType.LAZY)
    private List<Soldier> soldiers;
}
