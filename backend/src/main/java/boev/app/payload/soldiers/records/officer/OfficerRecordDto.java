package boev.app.payload.soldiers.records.officer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerRecordDto {
    private long rankId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate leadershipTrainingDate;
    private String academyName;
}
