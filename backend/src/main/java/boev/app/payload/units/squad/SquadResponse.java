package boev.app.payload.units.squad;

import boev.app.payload.soldiers.solider.SoldierMinimumDto;
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
public class SquadResponse extends MilitaryUnitResponse {
    private List<SoldierMinimumDto> soldiersMinimumDto;
    private String platoonName;
    private Long platoonId;
}
