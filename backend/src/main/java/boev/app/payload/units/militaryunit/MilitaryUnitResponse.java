package boev.app.payload.units.militaryunit;

import boev.app.payload.soldiers.solider.SoldierMinimumDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MilitaryUnitResponse {
    protected String name;
    protected SoldierMinimumDto commanderMinimum;
    protected Long id;
}
