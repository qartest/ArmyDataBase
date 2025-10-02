package boev.app.payload.armaments.type.technique.InfatryFightVehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class InfantryFightingVehicleTypeDto {
    private String model;
    private Integer gunCaliber;
    private Integer armorThickness;
    private Integer passengerCapacity;
}
