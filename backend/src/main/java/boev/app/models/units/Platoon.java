package boev.app.models.units;

import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.Soldier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "platoons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Platoon extends MilitaryUnit {
    @OneToMany(mappedBy = "platoon", fetch = FetchType.LAZY)
    private List<Squad> squads;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

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
