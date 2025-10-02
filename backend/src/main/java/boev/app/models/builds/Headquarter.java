package boev.app.models.builds;

import boev.app.models.units.MilitaryFormation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "headquarters")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Headquarter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "headquarter", fetch = FetchType.LAZY)
    List<Building> buildings;

    @OneToMany(mappedBy = "headquarter", fetch = FetchType.LAZY)
    List<MilitaryFormation> formations;


}
