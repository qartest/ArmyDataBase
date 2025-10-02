package boev.app.models.armaments.instances.fireHelp;
import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.type.fireHelp.MissileWeaponType;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "missile_weapons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MissileWeapon extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "launcher_status")
    @Enumerated(EnumType.STRING)
    private Condition launcherStatus; // Состояние пусковой установки (GOOD, BROKEN)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private MissileWeaponType type;

}
