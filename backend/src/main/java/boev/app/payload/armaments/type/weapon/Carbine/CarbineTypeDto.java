package boev.app.payload.armaments.type.weapon.Carbine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbineTypeDto {
    private String model;
    private Double caliber;
    private Integer effectiveRange;
    private Double weight;
}
