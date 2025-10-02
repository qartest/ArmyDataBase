package boev.app.payload.units.militaryFormation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentType {
    private String typeName; // Название основного типа (например, "Оружие", "Броня")
    private List<EquipmentSubType> subTypes; // Список подтипов
}