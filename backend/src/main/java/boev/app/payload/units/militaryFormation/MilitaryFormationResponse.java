package boev.app.payload.units.militaryFormation;

import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MilitaryFormationResponse extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> companiesId;
    private Long divisionId;
    private List<Long> equipmentsId;
    private long headquarterId;
}
