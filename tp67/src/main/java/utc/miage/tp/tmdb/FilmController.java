package utc.miage.tp.tmdb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/films")
public class FilmController {

  private final FilmService filmService;

  public FilmController(FilmService filmService) {
    this.filmService = filmService;
  }

  @GetMapping("/popular")
  public FilmPageDTO getPopularFilms(@RequestParam(defaultValue = "1") int page) {
    return filmService.getPopularFilms(page);
  }

  @GetMapping("/{id}")
  public FilmDTO getFilmById(@PathVariable long id) {
    return filmService.getFilmById(id);
  }
}
