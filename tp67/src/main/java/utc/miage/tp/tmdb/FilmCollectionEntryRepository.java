package utc.miage.tp.tmdb;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmCollectionEntryRepository extends JpaRepository<FilmCollectionEntry, Long> {

  boolean existsByUserIdAndFilmIdAndCollectionType(long userId, long filmId, FilmCollectionType collectionType);

  long countByUserIdAndCollectionType(long userId, FilmCollectionType collectionType);

  List<FilmCollectionEntry> findAllByUserIdAndCollectionTypeOrderByIdDesc(long userId, FilmCollectionType collectionType);
}
