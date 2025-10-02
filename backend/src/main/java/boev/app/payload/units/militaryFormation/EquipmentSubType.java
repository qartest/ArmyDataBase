package boev.app.payload.units.militaryFormation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSubType {
    private String name; // Название подтипа (например, "Автомат Калашникова")
    private long quantity; // Количество этого подтипа
}
