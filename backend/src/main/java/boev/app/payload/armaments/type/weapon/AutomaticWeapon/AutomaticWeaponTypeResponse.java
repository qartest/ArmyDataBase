package boev.app.payload.armaments.type.weapon.AutomaticWeapon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomaticWeaponTypeResponse {
    private long id;
    private String model;
    private Double caliber;
    private Integer fireRate;
    private Integer magazineCapacity;
}
