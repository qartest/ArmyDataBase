package boev.app.models.armaments.type.weapon;
import boev.app.models.armaments.instances.weapon.Carbine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "carbine_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CarbineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model; // Например, СКС

    @Column(name = "caliber")
    private Double caliber; // Калибр (мм)

    @Column(name = "effective_range")
    private Integer effectiveRange; // Эффективная дальность (м)

    @Column(name = "weight")
    private Double weight; // Вес (кг)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Carbine> equipment;
}
