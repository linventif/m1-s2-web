package utc.miage.tp.tmdb;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FilmService {

  private final RestTemplate restTemplate;
  private final FilmCollectionEntryRepository filmCollectionEntryRepository;
  private final String apiKey;
  private final String baseUrl;

  public FilmService(
      RestTemplate restTemplate,
      FilmCollectionEntryRepository filmCollectionEntryRepository,
      @Value("${tmdb.api-key:${tmdb.api.key}}") String apiKey,
      @Value("${tmdb.base-url:${tmdb.api.base-url}}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.filmCollectionEntryRepository = filmCollectionEntryRepository;
    this.apiKey = apiKey;
    this.baseUrl = baseUrl;
  }

  public FilmPageDTO getPopularFilms(int page) {
    int sanitizedPage = Math.max(page, 1);
    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path("/movie/popular")
        .queryParam("api_key", "{apiKey}")
        .queryParam("page", "{page}")
        .build(false)
        .toUriString();

    FilmPageDTO response =
        restTemplate.getForObject(url, FilmPageDTO.class, Map.of("apiKey", apiKey, "page", sanitizedPage));

    if (response == null) {
      throw new IllegalStateException("TMDB returned an empty response for popular films.");
    }

    return response;
  }

  public FilmDTO getFilmById(long id) {
    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path("/movie/{id}")
        .queryParam("api_key", "{apiKey}")
        .build(false)
        .toUriString();

    FilmDTO response = restTemplate.getForObject(url, FilmDTO.class, Map.of("id", id, "apiKey", apiKey));

    if (response == null) {
      throw new IllegalStateException("TMDB returned an empty response for film " + id + ".");
    }

    return response;
  }

  public void addToFavorites(long id) {
    saveIfMissing(id, FilmCollectionType.FAVORITE);
  }

  public void addToWatchlist(long id) {
    saveIfMissing(id, FilmCollectionType.WATCHLIST);
  }

  public boolean isFavorite(long id) {
    return filmCollectionEntryRepository.existsByFilmIdAndCollectionType(id, FilmCollectionType.FAVORITE);
  }

  public boolean isInWatchlist(long id) {
    return filmCollectionEntryRepository.existsByFilmIdAndCollectionType(id, FilmCollectionType.WATCHLIST);
  }

  public int favoriteCount() {
    return Math.toIntExact(filmCollectionEntryRepository.countByCollectionType(FilmCollectionType.FAVORITE));
  }

  public int watchlistCount() {
    return Math.toIntExact(filmCollectionEntryRepository.countByCollectionType(FilmCollectionType.WATCHLIST));
  }

  private void saveIfMissing(long filmId, FilmCollectionType collectionType) {
    if (filmCollectionEntryRepository.existsByFilmIdAndCollectionType(filmId, collectionType)) {
      return;
    }

    filmCollectionEntryRepository.save(new FilmCollectionEntry(filmId, collectionType));
  }
}
