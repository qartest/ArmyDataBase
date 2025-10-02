package boev.app.payload.armaments.instances.weapon.AutomaticWeapon;

import boev.app.models.armaments.Condition;
import boev.app.payload.armaments.instances.equipment.EquipmentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticWeaponResponse extends EquipmentResponse {
    private String serialNumber;
    private Condition stockCondition;
    private long typeId;
}
