package utc.miage.tp.tmdb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import utc.miage.tp.user.AppUser;

@Entity
@Table(
    name = "film_collection_entries",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "film_id", "collection_type"}))
public class FilmCollectionEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "film_id", nullable = false)
  private Long filmId;

  @ManyToOne(optional = true)
  @JoinColumn(name = "user_id")
  private AppUser user;

  @Enumerated(EnumType.STRING)
  @Column(name = "collection_type", nullable = false, length = 32)
  private FilmCollectionType collectionType;

  protected FilmCollectionEntry() {
    // Required by JPA.
  }

  public FilmCollectionEntry(AppUser user, Long filmId, FilmCollectionType collectionType) {
    this.user = user;
    this.filmId = filmId;
    this.collectionType = collectionType;
  }

  public Long getId() {
    return id;
  }

  public Long getFilmId() {
    return filmId;
  }

  public AppUser getUser() {
    return user;
  }

  public FilmCollectionType getCollectionType() {
    return collectionType;
  }
}
