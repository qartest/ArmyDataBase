package boev.app.payload.builds.building;

import boev.app.models.builds.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDto {
    private String address;
    private BuildingType type;
    private Long headquarterId;
    private String name;
}
