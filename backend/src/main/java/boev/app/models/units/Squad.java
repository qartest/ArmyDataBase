package boev.app.models.units;

import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.Soldier;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "squads")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squad extends MilitaryUnit {
    @OneToMany(mappedBy = "squad", fetch = FetchType.LAZY)
    private List<Soldier> soldiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platoon_id")
    private Platoon platoon;

    @PrePersist
    @PreUpdate
    public void validate() {
        if(commander != null){
            if (commander.getRank().getCategory() != RankCategory.SERGEANT && commander.getRank().getCategory() != RankCategory.OFFICER) {
                throw new IllegalArgumentException("Командир взвода должен быть офицером или сержантом");
            }
        }
    }
}

