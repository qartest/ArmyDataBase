package boev.app.payload.soldiers.rank;

import boev.app.models.soldiers.RankCategory;
import boev.app.models.soldiers.RussianArmyRank;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankResponse {
    private long id;
    private String name; // Русское название звания
    private RankCategory category;
    private int hierarchyLevel; // Для сортировки
}
