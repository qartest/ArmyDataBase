package boev.app.payload.armaments.type.technique.Truck;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruckTypeResponse {
    private long id;
    private String model;
    private Double payloadCapacity;
    private Integer passengerCapacity;
}
