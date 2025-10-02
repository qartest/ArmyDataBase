package boev.app.payload.units.company;

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
public class CompanyResponse extends MilitaryUnitResponse {
    private List<MilitaryUnitResponse> platoons;
    private Long formationId;
    private String formationName;
}
