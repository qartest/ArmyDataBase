package boev.app.payload.soldiers.records.privates;

import boev.app.models.soldiers.Rank;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateRecordDto {
    private Long rankId;
    private LocalDate startDate;
    private LocalDate endDate;
}
