package utc.miage.tp.workout;

import java.sql.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import utc.miage.tp.sport.Sport;
import utc.miage.tp.user.User;
import jakarta.persistence.Id;

@Entity
@Table(name = "workout")
public class Workout {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private Long id;

  @Column(name = "date", nullable = false)
  @ColumnDefault("current_date")
  private Date date;

	@Column(name = "distance", nullable = false)
  @ColumnDefault("0")
  private Double distance;

  @Column(name = "duration", nullable = false)
  @ColumnDefault("0")
  private Double duration;

  public Workout(Date date, Double distance, Double duration) {// , Sport sport, User user) {
    this.date = date;
    this.distance = distance;
    this.duration = duration;
  }

  public Long getId() {
    return id;
  }

  public Date getDate() {
    return date;
  }

  public Double getDistance() {
    return distance;
  }

  public Double getDuration() {
    return duration;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

}
