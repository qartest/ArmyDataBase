package boev.app.payload.units.militaryFormation;

import boev.app.models.armaments.instances.Equipment;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilitaryFormationResponseLight extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> companies;
    private Long divisionId;
    private String divisionName;
    private String headquarterName;
    private long headquarterId;
    private List<EquipmentType> subType;
}
