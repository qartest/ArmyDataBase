package boev.app.payload.armaments.instances.fireHelp.antiaircraft;

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
public class AntiaircraftResponse extends EquipmentResponse {
    private String serialNumber;
    private Condition radarStatus;
    private long typeId;
}
