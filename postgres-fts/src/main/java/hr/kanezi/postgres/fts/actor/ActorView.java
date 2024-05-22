package hr.kanezi.postgres.fts.actor;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "actors")
@Data
@NoArgsConstructor
@Getter
@Setter
public class ActorView {
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
}
