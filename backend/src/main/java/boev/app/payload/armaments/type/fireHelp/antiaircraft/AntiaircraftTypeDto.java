package boev.app.payload.armaments.type.fireHelp.antiaircraft;

import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AntiaircraftTypeDto {
    private String model;
    private Integer range; // Дальность (км)
    private Integer targetAltitude; // Максимальная высота цели (м)
}
