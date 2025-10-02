package boev.app.models.armaments.instances.technique;

import boev.app.models.armaments.Condition;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.type.technique.TankType;
import boev.app.models.armaments.type.technique.TruckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "tanks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tank extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "tank_kills")
    private Integer tankKills;

    @Column(name = "gun_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition gunStatus;

    @Column(name = "track_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Condition trackStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private TankType type;

}
