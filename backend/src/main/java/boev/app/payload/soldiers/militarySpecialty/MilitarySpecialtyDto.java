package boev.app.payload.soldiers.militarySpecialty;

import boev.app.models.soldiers.specialty.MilitarySpecialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilitarySpecialtyDto {
    private MilitarySpecialty name;
}
