package utc.miage.tp.tmdb;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmCollectionEntryRepository extends JpaRepository<FilmCollectionEntry, Long> {

  boolean existsByFilmIdAndCollectionType(long filmId, FilmCollectionType collectionType);

  long countByCollectionType(FilmCollectionType collectionType);
}
