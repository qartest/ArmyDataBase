package boev.app.models.soldiers;


import boev.app.models.soldiers.specialty.MilitarySpecialtyEntity;
import boev.app.models.soldiers.records.OfficerRecord;
import boev.app.models.soldiers.records.PrivateRecord;
import boev.app.models.soldiers.records.SergeantRecord;
import boev.app.models.units.MilitaryUnit;
import boev.app.models.units.Squad;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "soldiers")
public class Soldier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "second_name", nullable = false)
    private String secondName;
    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "birth_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "soldier_specialties",
            joinColumns = @JoinColumn(name = "soldier_id"),
            inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    private Set<MilitarySpecialtyEntity> specialties;

    @OneToMany(mappedBy = "soldier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PrivateRecord> privateRecords; // Запись для рядового

    @OneToMany(mappedBy = "soldier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SergeantRecord> sergeantRecords; // Список записей для сержанта

    @OneToMany(mappedBy = "soldier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OfficerRecord> officerRecords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @OneToOne(mappedBy = "commander", fetch = FetchType.LAZY)
    private MilitaryUnit commandedUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id", nullable = false)
    private Rank rank;

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName); // Используем только основные поля
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Soldier other = (Soldier) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(firstName, other.firstName) &&
                Objects.equals(secondName, other.secondName);
    }
}
