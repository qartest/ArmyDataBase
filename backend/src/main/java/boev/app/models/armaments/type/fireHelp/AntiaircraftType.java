package boev.app.models.armaments.type.fireHelp;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "antiaircraft_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AntiaircraftType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model;

    @Column(name = "range")
    private Integer range; // Дальность (км)

    @Column(name = "target_altitude")
    private Integer targetAltitude; // Максимальная высота цели (м)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Antiaircraft> equipment;
}