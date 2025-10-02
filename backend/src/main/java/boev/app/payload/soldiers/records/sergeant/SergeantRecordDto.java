package boev.app.payload.soldiers.records.sergeant;

import boev.app.models.soldiers.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SergeantRecordDto {
    private long rankId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate leadershipTrainingDate;
}
