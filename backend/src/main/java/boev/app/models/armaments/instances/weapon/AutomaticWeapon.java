package boev.app.models.armaments.instances.weapon;

import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.type.fireHelp.AntiaircraftType;
import boev.app.models.armaments.type.weapon.AutomaticWeaponType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "automatic_weapons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AutomaticWeapon extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "stock_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition stockCondition; // Состояние приклада (GOOD, WORN, BROKEN)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private AutomaticWeaponType type;

}