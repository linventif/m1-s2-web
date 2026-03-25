package utc.miage.tp.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FilmDTO(
    long id,
    String title,
    @JsonProperty("original_title") String originalTitle,
    String overview,
    @JsonProperty("release_date") String releaseDate,
    @JsonProperty("poster_path") String posterPath,
    @JsonProperty("backdrop_path") String backdropPath,
    @JsonProperty("vote_average") Double voteAverage,
    @JsonProperty("vote_count") Integer voteCount,
    @JsonProperty("original_language") String originalLanguage,
    Double popularity,
    Boolean adult,
    Boolean video,
    @JsonProperty("genre_ids") List<Long> genreIds) {}
