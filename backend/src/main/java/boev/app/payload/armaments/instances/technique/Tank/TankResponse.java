package boev.app.payload.armaments.instances.technique.Tank;

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
public class TankResponse extends EquipmentResponse {
    private String serialNumber;
    private Integer tankKills;
    private Condition gunStatus;
    private Condition trackStatus;
    private long typeId;
}
