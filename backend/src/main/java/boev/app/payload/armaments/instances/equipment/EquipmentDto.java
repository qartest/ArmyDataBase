package boev.app.payload.armaments.instances.equipment;

import boev.app.models.armaments.EquipmentCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class EquipmentDto {
    protected EquipmentCategory category;
    protected LocalDate yearOfManufacture;
    protected long formationId;
}
