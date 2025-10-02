package boev.app.payload.units.militaryFormation;

import boev.app.payload.units.militaryunit.MilitaryUnitDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MilitaryFormationDto extends MilitaryUnitDto {
    private List<Long> companiesId;
    private Long divisionId;
    private List<Long> equipmentsId;
    private long headquarterId;
}
