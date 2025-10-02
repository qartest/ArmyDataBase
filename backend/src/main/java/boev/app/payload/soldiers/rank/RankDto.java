package boev.app.payload.soldiers.rank;

import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.RussianArmyRank;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RankDto {
    private RussianArmyRank name;
    private RankCategory category;
}
