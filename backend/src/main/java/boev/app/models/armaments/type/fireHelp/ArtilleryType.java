package boev.app.models.armaments.type.fireHelp;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.instances.fireHelp.Artillery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "artillery_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArtilleryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model; // Например, 2С19 Мста-С

    @Column(name = "caliber")
    private Integer caliber; // Калибр (мм)

    @Column(name = "range")
    private Integer range; // Дальность (км)

    @Column(name = "rate_of_fire")
    private Integer rateOfFire; // Скорострельность (выстр/мин)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Artillery> equipment;
}
