package boev.app.payload.armaments.type.fireHelp.missleWeapon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissileWeaponTypeDto {
    private String model; // Например, Искандер-М
    private Integer range;
    private String warheadType;
    private String guidanceSystem;
}
