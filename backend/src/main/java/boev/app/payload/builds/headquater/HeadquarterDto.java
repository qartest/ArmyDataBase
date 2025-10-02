package boev.app.payload.builds.headquater;

import boev.app.models.builds.Building;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterDto {
    private String address;
    private String name;
}
