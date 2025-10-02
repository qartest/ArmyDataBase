package boev.app.models.armaments.type.fireHelp;

import boev.app.models.armaments.instances.fireHelp.MissileWeapon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "missile_weapon_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MissileWeaponType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model; // Например, Искандер-М

    @Column(name = "range")
    private Integer range; // Дальность (км)

    @Column(name = "warhead_type")
    private String warheadType; // Тип боеголовки (CONVENTIONAL, NUCLEAR)

    @Column(name = "guidance_system")
    private String guidanceSystem; // Система наведения (INERTIAL, GPS)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MissileWeapon> equipment;
}

