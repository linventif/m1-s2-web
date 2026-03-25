package utc.miage.tp.tmdb;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utc.miage.tp.user.AppUser;
import utc.miage.tp.user.UserService;

@Controller
@RequestMapping("/films")
public class FilmPageController {

  private final FilmService filmService;
  private final UserService userService;

  public FilmPageController(FilmService filmService, UserService userService) {
    this.filmService = filmService;
    this.userService = userService;
  }

  @GetMapping({"", "/list"})
  public String showPopularFilms(
      @RequestParam(defaultValue = "1") int page,
      HttpSession session,
      Model model) {
    FilmPageDTO filmPage = filmService.getPopularFilms(page);
    List<FilmDTO> films = filmPage.results() == null ? List.of() : filmPage.results();
    Optional<AppUser> user = userService.findLoggedUser(session);

    model.addAttribute("filmPage", filmPage);
    model.addAttribute("films", films);
    model.addAttribute("currentPage", filmPage.page());
    model.addAttribute("hasPrevious", filmPage.page() > 1);
    model.addAttribute("hasNext", filmPage.page() < filmPage.totalPages());
    model.addAttribute("favoriteCount", filmService.favoriteCount(user));
    model.addAttribute("watchlistCount", filmService.watchlistCount(user));
    return "films/list";
  }

  @GetMapping("/{id:[0-9]+}")
  public String showFilmDetail(@PathVariable long id, HttpSession session, Model model) {
    FilmDTO film = filmService.getFilmById(id);
    Optional<AppUser> user = userService.findLoggedUser(session);

    model.addAttribute("film", film);
    model.addAttribute("isFavorite", filmService.isFavorite(user, id));
    model.addAttribute("isInWatchlist", filmService.isInWatchlist(user, id));
    model.addAttribute("favoriteCount", filmService.favoriteCount(user));
    model.addAttribute("watchlistCount", filmService.watchlistCount(user));
    return "films/detail";
  }

  @GetMapping("/my-list")
  public String showMyList(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
    Optional<AppUser> user = userService.findLoggedUser(session);
    if (user.isEmpty()) {
      redirectAttributes.addFlashAttribute("feedbackMessage", "Connecte-toi pour voir tes listes.");
      redirectAttributes.addFlashAttribute("feedbackType", "info");
      return "redirect:/auth/login";
    }

    model.addAttribute("favoriteFilms", filmService.getUserSelections(user.get(), FilmCollectionType.FAVORITE));
    model.addAttribute("watchlistFilms", filmService.getUserSelections(user.get(), FilmCollectionType.WATCHLIST));
    model.addAttribute("favoriteCount", filmService.favoriteCount(user));
    model.addAttribute("watchlistCount", filmService.watchlistCount(user));
    return "films/my-list";
  }

  @PostMapping("/{id:[0-9]+}/favorites")
  public String addToFavorites(
      @PathVariable long id,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    Optional<AppUser> user = userService.findLoggedUser(session);
    if (user.isEmpty()) {
      redirectAttributes.addFlashAttribute("feedbackMessage", "Connecte-toi pour ajouter des favoris.");
      redirectAttributes.addFlashAttribute("feedbackType", "info");
      return "redirect:/auth/login?redirect=/films/" + id;
    }

    filmService.addToFavorites(user.get(), id);
    redirectAttributes.addFlashAttribute("feedbackMessage", "Film ajoute aux favoris.");
    redirectAttributes.addFlashAttribute("feedbackType", "success");
    return "redirect:/films/" + id;
  }

  @PostMapping("/{id:[0-9]+}/watchlist")
  public String addToWatchlist(
      @PathVariable long id,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    Optional<AppUser> user = userService.findLoggedUser(session);
    if (user.isEmpty()) {
      redirectAttributes.addFlashAttribute("feedbackMessage", "Connecte-toi pour ajouter des films a voir.");
      redirectAttributes.addFlashAttribute("feedbackType", "info");
      return "redirect:/auth/login?redirect=/films/" + id;
    }

    filmService.addToWatchlist(user.get(), id);
    redirectAttributes.addFlashAttribute("feedbackMessage", "Film ajoute a la liste a voir.");
    redirectAttributes.addFlashAttribute("feedbackType", "info");
    return "redirect:/films/" + id;
  }
}
