package boev.app.payload.soldiers.solider;

import boev.app.models.soldiers.records.OfficerRecord;
import boev.app.models.soldiers.records.PrivateRecord;
import boev.app.models.soldiers.records.SergeantRecord;
import boev.app.payload.soldiers.records.officer.OfficerRecordResponse;
import boev.app.payload.soldiers.records.privates.PrivateRecordResponse;
import boev.app.payload.soldiers.records.sergeant.SergeantRecordResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldierSimpleDto {
    private Long id;
    private String firstName;
    private String secondName;
    private String fatherName;
    private LocalDate birthDay;
    
    private List<String> specialtyNames;
    private List<Long> specialtyId;

    private String squadName;
    private Long squadId;

    private String commandedUnitName;
    private Long commandedUnitId;

    private String rankName;
    private Long rankID;

    private List<PrivateRecordResponse> privateRecords;
    private List<SergeantRecordResponse> sergeantRecords;
    private List<OfficerRecordResponse> officerRecords;
}
