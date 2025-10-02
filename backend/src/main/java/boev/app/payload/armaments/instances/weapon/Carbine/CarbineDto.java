package boev.app.payload.armaments.instances.weapon.Carbine;

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
public class CarbineDto extends EquipmentDto {
    private String serialNumber;
    private Condition sightCondition;
    private int kills;
    private long typeId;
}
