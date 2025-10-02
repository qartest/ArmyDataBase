package boev.app.payload.units.platoon;

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
public class PlatoonDto extends MilitaryUnitDto {
    private List<Long> squadsId;
    private Long companyId;
}
