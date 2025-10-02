package boev.app.models.armaments.type.technique;
import boev.app.models.armaments.instances.technique.InfantryFightingVehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "infantry_fighting_vehicle_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InfantryFightingVehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model; // Например, БМП-2

    @Column(name = "gun_caliber")
    private Integer gunCaliber; // Калибр орудия (мм)

    @Column(name = "armor_thickness")
    private Integer armorThickness; // Толщина брони (мм)

    @Column(name = "passenger_capacity")
    private Integer passengerCapacity; // Вместимость (человек)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<InfantryFightingVehicle> equipment;
}