package utc.miage.tp.workout;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "workout")
public class Workout {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

	@Column(name = "distance", nullable = false)
  @ColumnDefault("0")
  private Double distance;

  @Column(name = "duration", nullable = false)
  @ColumnDefault("0")
  private Double duration;
}
