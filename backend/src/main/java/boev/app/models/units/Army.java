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
@Table(name = "armies")
public class Army extends MilitaryUnit {

    @OneToMany(mappedBy = "army", fetch = FetchType.LAZY)
    private List<Corp> corps;

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


