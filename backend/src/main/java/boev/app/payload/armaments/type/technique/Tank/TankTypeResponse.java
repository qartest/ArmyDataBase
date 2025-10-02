package boev.app.payload.armaments.type.technique.Tank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TankTypeResponse {
    private long id;
    private String model;
    private Integer gunCaliber;
    private Double weight;
    private Integer maxSpeed;
}
