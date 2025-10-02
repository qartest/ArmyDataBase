package boev.app.models.armaments.instances;

import boev.app.models.armaments.EquipmentCategory;
import boev.app.models.units.MilitaryFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected EquipmentCategory category;

    @Column(name = "date_of_manufacture")
    protected LocalDate yearOfManufacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id")
    protected MilitaryFormation formation;
}