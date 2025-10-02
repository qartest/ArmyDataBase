package boev.app.payload.units.division;

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
public class DivisionResponse extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> formations;
    private String corpName;
    private Long corpId;
}
