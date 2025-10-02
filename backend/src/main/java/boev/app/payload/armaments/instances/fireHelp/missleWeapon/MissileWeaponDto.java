package boev.app.payload.armaments.instances.fireHelp.missleWeapon;

import boev.app.models.armaments.Condition;
import boev.app.payload.armaments.instances.equipment.EquipmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissileWeaponDto extends EquipmentDto {
    private String serialNumber;
    private Condition launcherStatus;
    private long typeId;
}
