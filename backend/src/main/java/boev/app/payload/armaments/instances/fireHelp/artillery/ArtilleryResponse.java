package boev.app.payload.armaments.instances.fireHelp.artillery;

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
public class ArtilleryResponse extends EquipmentResponse {
    private String serialNumber;
    private Integer shots;
    private Condition barrelStatus;
    private long typeId;
}
