package boev.app.models.armaments.instances.weapon;
import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.type.weapon.CarbineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
// Класс для карабина
@Entity
@Table(name = "carbines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Carbine extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "sight_condition")
    @Enumerated(EnumType.STRING)
    private Condition sightCondition; // Состояние прицела (GOOD, DAMAGED)

    @Column(name = "kills")
    private int kills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private CarbineType type;

}
