package utc.miage.tp.sport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "sport")
public class Sport {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  public Sport(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public Long getID() {
    return this.id;
  }
}
