package boev.app.payload.soldiers.records.sergeant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SergeantRecordResponse {
    private long id;
    private long rankId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate leadershipTrainingDate;
}
