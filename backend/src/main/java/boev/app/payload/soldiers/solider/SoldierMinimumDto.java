package boev.app.payload.soldiers.solider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldierMinimumDto {
    private long id;
    private String firstName;
    private String secondName;
    private String fatherName;
    private String rankName;
}
