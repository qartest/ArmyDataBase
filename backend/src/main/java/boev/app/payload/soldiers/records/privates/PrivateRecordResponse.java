package boev.app.payload.soldiers.records.privates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateRecordResponse {
    private long id;
    private Long rankId;
    private LocalDate startDate;
    private LocalDate endDate;
}
