package boev.app.models.armaments.type.technique;
import boev.app.models.armaments.instances.technique.Tank;
import boev.app.models.armaments.instances.technique.Truck;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Table(name = "truck_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TruckType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model;

    @Column(name = "payload_capacity")
    private Double payloadCapacity; // Грузоподъёмность (т)

    @Column(name = "passenger_capacity")
    private Integer passengerCapacity; // Вместимость (человек)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Truck> equipment;
}
