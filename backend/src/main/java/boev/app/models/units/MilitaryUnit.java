package boev.app.models.units;

import jakarta.persistence.OneToOne;
import boev.app.models.soldiers.Soldier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class MilitaryUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "commander_id", nullable = true)
    protected Soldier commander;

    @Override
    public int hashCode() {
        return Objects.hash(id, name); // Исключаем commander
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MilitaryUnit other = (MilitaryUnit) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(name, other.name);
    }
}
