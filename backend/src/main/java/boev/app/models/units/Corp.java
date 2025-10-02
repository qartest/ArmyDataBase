package boev.app.models.units;

import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.Soldier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "corps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Corp extends MilitaryUnit {

    @OneToMany(mappedBy = "corp", fetch = FetchType.LAZY)
    private List<Division> divisions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "army_id")
    private Army army;

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