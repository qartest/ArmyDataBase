package boev.app.models.armaments.instances.technique;
import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.type.technique.InfantryFightingVehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "infantry_fighting_vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InfantryFightingVehicle extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "vehicle_condition")
    @Enumerated(EnumType.STRING)
    private Condition vehicleCondition; // Состояние башни (GOOD, DAMAGED)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private InfantryFightingVehicleType type;


}
