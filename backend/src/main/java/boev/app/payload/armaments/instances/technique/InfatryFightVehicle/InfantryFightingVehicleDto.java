package boev.app.payload.armaments.instances.technique.InfatryFightVehicle;

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
public class InfantryFightingVehicleDto extends EquipmentDto {
    private String serialNumber;
    private Condition vehicleCondition;
    private long typeId;
}
