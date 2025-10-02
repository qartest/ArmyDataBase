package boev.app.payload.builds.headquater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadquarterResponseSimple {
    private long id;
    private String address;
    private String name;
}
