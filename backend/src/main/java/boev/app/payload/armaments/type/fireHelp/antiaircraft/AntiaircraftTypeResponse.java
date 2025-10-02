package boev.app.payload.armaments.type.fireHelp.antiaircraft;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntiaircraftTypeResponse {
    private long id;
    private String model;
    private Integer range; // Дальность (км)
    private Integer targetAltitude; // Максимальная высота цели (м)
}
