package boev.app.models.armaments.type.weapon;
import boev.app.models.armaments.instances.weapon.AutomaticWeapon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "automatic_weapon_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AutomaticWeaponType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "model", nullable = false)
    @EqualsAndHashCode.Include
    private String model;

    @Column(name = "caliber")
    private Double caliber; // Калибр (мм)

    @Column(name = "fire_rate")
    private Integer fireRate; // Скорострельность (выстр/мин)

    @Column(name = "magazine_capacity")
    private Integer magazineCapacity; // Ёмкость магазина (патронов)

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AutomaticWeapon> equipment;
}
