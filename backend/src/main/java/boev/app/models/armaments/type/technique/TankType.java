package boev.app.models.armaments.type.technique;

import boev.app.models.armaments.instances.technique.Tank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tank_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TankType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model;

    @Column(name = "gun_caliber")
    private Integer gunCaliber; // Калибр орудия (мм)

    @Column(name = "weight")
    private Double weight; // Вес (т)

    @Column(name = "max_speed")
    private Integer maxSpeed; // Максимальная скорость (км/ч)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Tank> equipment;
}