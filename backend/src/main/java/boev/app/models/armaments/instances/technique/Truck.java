package boev.app.models.armaments.instances.technique;


import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.type.technique.TruckType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "trucks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Truck extends Equipment {
    @Column(name = "serial_number", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String serialNumber;

    @Column(name = "mileage")
    private Integer mileage; // Пробег (км)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private TruckType type;

}

