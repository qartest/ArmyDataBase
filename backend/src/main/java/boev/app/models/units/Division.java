package boev.app.models.units;

import boev.app.models.soldiers.RankCategory;
import jakarta.persistence.OneToOne;
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
@Table(name = "divisions")
public class Division extends MilitaryUnit {

    @OneToMany(mappedBy = "division", fetch = FetchType.LAZY)
    private List<MilitaryFormation> formations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corp_id")
    private Corp corp;

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
