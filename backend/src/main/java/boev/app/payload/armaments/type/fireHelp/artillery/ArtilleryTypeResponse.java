package boev.app.payload.armaments.type.fireHelp.artillery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtilleryTypeResponse {
    private long id;
    private String model; // Например, 2С19 Мста-С
    private Integer caliber; // Калибр (мм)
    private Integer range; // Дальность (км)
    private Integer rateOfFire;
}
