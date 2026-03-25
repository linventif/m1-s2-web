package utc.miage.tp.tmdb;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utc.miage.tp.user.AppUser;
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

  public void addToFavorites(AppUser user, long id) {
    saveIfMissing(user, id, FilmCollectionType.FAVORITE);
  }

  public void addToWatchlist(AppUser user, long id) {
    saveIfMissing(user, id, FilmCollectionType.WATCHLIST);
  }

  public boolean isFavorite(Optional<AppUser> user, long id) {
    return user.isPresent()
        && filmCollectionEntryRepository.existsByUserIdAndFilmIdAndCollectionType(
            user.get().getId(), id, FilmCollectionType.FAVORITE);
  }

  public boolean isInWatchlist(Optional<AppUser> user, long id) {
    return user.isPresent()
        && filmCollectionEntryRepository.existsByUserIdAndFilmIdAndCollectionType(
            user.get().getId(), id, FilmCollectionType.WATCHLIST);
  }

  public int favoriteCount(Optional<AppUser> user) {
    return user.map(value ->
            Math.toIntExact(filmCollectionEntryRepository.countByUserIdAndCollectionType(
                value.getId(), FilmCollectionType.FAVORITE)))
        .orElse(0);
  }

  public int watchlistCount(Optional<AppUser> user) {
    return user.map(value ->
            Math.toIntExact(filmCollectionEntryRepository.countByUserIdAndCollectionType(
                value.getId(), FilmCollectionType.WATCHLIST)))
        .orElse(0);
  }

  public List<FilmDTO> getUserSelections(AppUser user, FilmCollectionType collectionType) {
    return filmCollectionEntryRepository
        .findAllByUserIdAndCollectionTypeOrderByIdDesc(user.getId(), collectionType)
        .stream()
        .map(entry -> getFilmById(entry.getFilmId()))
        .toList();
  }

  private void saveIfMissing(AppUser user, long filmId, FilmCollectionType collectionType) {
    if (filmCollectionEntryRepository.existsByUserIdAndFilmIdAndCollectionType(
        user.getId(), filmId, collectionType)) {
      return;
    }

    filmCollectionEntryRepository.save(new FilmCollectionEntry(user, filmId, collectionType));
  }
}
