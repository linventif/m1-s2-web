package utc.miage.tp.tmdb;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FilmService {

  private final RestTemplate restTemplate;
  private final String apiKey;
  private final String baseUrl;

  public FilmService(
      RestTemplate restTemplate,
      @Value("${tmdb.api-key:${tmdb.api.key}}") String apiKey,
      @Value("${tmdb.base-url:${tmdb.api.base-url}}") String baseUrl) {
    this.restTemplate = restTemplate;
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
}
