package boev.app.payload.soldiers.solider;

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
public class SoldierResponse {

    private long id;
    private String firstName;
    private String secondName;
    private String fatherName;

    private LocalDate birthDay;
    private List<Long> specialtiesId;

    private List<PrivateRecordResponse> privateRecords;
    private List<SergeantRecordResponse> sergeantRecords;
    private List<OfficerRecordResponse> officerRecords;

    private Long squadId;
    private Long MilitaryUnitId;

    private Long rankId;
}
