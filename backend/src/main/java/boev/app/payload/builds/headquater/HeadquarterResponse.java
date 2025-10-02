package boev.app.payload.builds.headquater;

import boev.app.payload.builds.building.BuildingResponse;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterResponse {
    private long id;
    private String address;
    private String name;
    List<BuildingResponse> buildings;
    List<MilitaryUnitResponse> formations;
}
