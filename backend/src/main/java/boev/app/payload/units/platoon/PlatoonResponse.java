package boev.app.payload.units.platoon;


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
public class PlatoonResponse extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> squads;
    private String companyName;
    private Long companyId;
}
