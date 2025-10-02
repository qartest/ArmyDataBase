package boev.app.payload.builds.building;

import boev.app.models.builds.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingResponse {
    private long id;
    private String address;
    private BuildingType type;
    private Long headquarterId;
    private String headquarterName;
    private String name;
}
