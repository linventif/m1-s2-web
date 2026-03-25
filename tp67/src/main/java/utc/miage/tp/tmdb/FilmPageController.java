package utc.miage.tp.tmdb;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/films")
public class FilmPageController {

  private final FilmService filmService;

  public FilmPageController(FilmService filmService) {
    this.filmService = filmService;
  }

  @GetMapping({"", "/list"})
  public String showPopularFilms(@RequestParam(defaultValue = "1") int page, Model model) {
    FilmPageDTO filmPage = filmService.getPopularFilms(page);
    List<FilmDTO> films = filmPage.results() == null ? List.of() : filmPage.results();

    model.addAttribute("filmPage", filmPage);
    model.addAttribute("films", films);
    model.addAttribute("currentPage", filmPage.page());
    model.addAttribute("hasPrevious", filmPage.page() > 1);
    model.addAttribute("hasNext", filmPage.page() < filmPage.totalPages());
    model.addAttribute("favoriteCount", filmService.favoriteCount());
    model.addAttribute("watchlistCount", filmService.watchlistCount());
    return "films/list";
  }

  @GetMapping("/{id:[0-9]+}")
  public String showFilmDetail(@PathVariable long id, Model model) {
    FilmDTO film = filmService.getFilmById(id);

    model.addAttribute("film", film);
    model.addAttribute("isFavorite", filmService.isFavorite(id));
    model.addAttribute("isInWatchlist", filmService.isInWatchlist(id));
    model.addAttribute("favoriteCount", filmService.favoriteCount());
    model.addAttribute("watchlistCount", filmService.watchlistCount());
    return "films/detail";
  }

  @PostMapping("/{id:[0-9]+}/favorites")
  public String addToFavorites(
      @PathVariable long id,
      RedirectAttributes redirectAttributes) {
    filmService.addToFavorites(id);
    redirectAttributes.addFlashAttribute("feedbackMessage", "Film ajoute aux favoris.");
    redirectAttributes.addFlashAttribute("feedbackType", "success");
    return "redirect:/films/" + id;
  }

  @PostMapping("/{id:[0-9]+}/watchlist")
  public String addToWatchlist(
      @PathVariable long id,
      RedirectAttributes redirectAttributes) {
    filmService.addToWatchlist(id);
    redirectAttributes.addFlashAttribute("feedbackMessage", "Film ajoute a la liste a voir.");
    redirectAttributes.addFlashAttribute("feedbackType", "info");
    return "redirect:/films/" + id;
  }
}
