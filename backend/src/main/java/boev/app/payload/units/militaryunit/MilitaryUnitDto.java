package boev.app.payload.units.militaryunit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MilitaryUnitDto {
    protected String name;
    protected Long commanderId;
}
