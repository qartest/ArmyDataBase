package boev.app.payload.units.militaryFormation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentAll {
    private List<EquipmentType> subType;
}
