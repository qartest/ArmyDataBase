package boev.app.payload.armaments.instances.technique.Truck;

import boev.app.payload.armaments.instances.equipment.EquipmentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruckDto extends EquipmentDto {
    private String serialNumber;
    private Integer mileage;
    private long typeId;
}
