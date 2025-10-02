package boev.app.payload.units.corp;

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
public class CorpResponse extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> divisions;
    private Long armyId;
    private String armyName;

}
