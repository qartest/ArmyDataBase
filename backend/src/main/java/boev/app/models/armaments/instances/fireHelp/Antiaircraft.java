package boev.app.models.armaments.instances.fireHelp;
import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.type.fireHelp.AntiaircraftType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "antiaircraft")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Antiaircraft extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "radar_status")
    @Enumerated(EnumType.STRING)
    private Condition radarStatus; // Состояние радара (GOOD, BROKEN)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private AntiaircraftType type;

}