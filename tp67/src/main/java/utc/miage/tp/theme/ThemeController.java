package utc.miage.tp.theme;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThemeController {

  @PostMapping("/theme")
  public String updateTheme(
      @RequestParam String theme,
      @RequestParam(defaultValue = "/") String redirect,
      HttpServletResponse response) {
    String selectedTheme = ThemeCatalog.isValid(theme) ? theme : ThemeCatalog.DEFAULT_THEME;

    Cookie cookie = new Cookie("theme", selectedTheme);
    cookie.setPath("/");
    cookie.setHttpOnly(false);
    cookie.setMaxAge(60 * 60 * 24 * 365);
    response.addCookie(cookie);

    if (redirect == null || redirect.isBlank() || !redirect.startsWith("/")) {
      return "redirect:/";
    }

    return "redirect:" + redirect;
  }
}
