package boev.app.models.units;

import boev.app.models.armaments.instances.Equipment;
import boev.app.models.builds.Headquarter;
import boev.app.models.soldiers.RankCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "military_formations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MilitaryFormation extends MilitaryUnit {

    @OneToMany(mappedBy = "formation", fetch = FetchType.LAZY)
    private List<Company> companies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;

    @PrePersist
    @PreUpdate
    public void validate() {
        if(commander != null){
            if (commander.getRank().getCategory() != RankCategory.OFFICER) {
                throw new IllegalArgumentException("Командир воинской части должен быть офицером");
            }
        }
    }
    @OneToMany(mappedBy = "formation", fetch = FetchType.LAZY)
    private List<Equipment> equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id", nullable = false)
    private Headquarter headquarter;
}


