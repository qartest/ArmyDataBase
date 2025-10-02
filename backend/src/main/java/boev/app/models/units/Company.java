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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends MilitaryUnit {
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Platoon> platoons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    private MilitaryFormation formation;

    @PrePersist
    @PreUpdate
    public void validate() {
        if(commander != null){
            if (commander.getRank().getCategory() != RankCategory.OFFICER) {
                throw new IllegalArgumentException("Командир воинской части должен быть офицером");
            }
        }
    }

}
