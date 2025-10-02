package boev.app.payload.soldiers.solider;

import boev.app.payload.soldiers.records.officer.OfficerRecordDto;
import boev.app.payload.soldiers.records.privates.PrivateRecordDto;
import boev.app.payload.soldiers.records.sergeant.SergeantRecordDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SoldierDto {
    @NotNull
    private String firstName;
    @NotNull
    private String secondName;
    private String fatherName;

    @NotNull
    private LocalDate birthDay;

    private List<PrivateRecordDto> privateRecords;
    private List<SergeantRecordDto> sergeantRecords;
    private List<OfficerRecordDto> officerRecords;

    private List<Long> specialtiesId;
    private Long squadId;
    private Long commandedUnitId;

    private long rankId;
}
