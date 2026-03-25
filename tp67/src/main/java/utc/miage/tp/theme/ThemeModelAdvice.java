package utc.miage.tp.theme;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ThemeModelAdvice {

  @ModelAttribute("themes")
  public java.util.List<String> themes() {
    return ThemeCatalog.ALL_THEMES;
  }

  @ModelAttribute("currentTheme")
  public String currentTheme(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("theme".equals(cookie.getName()) && ThemeCatalog.isValid(cookie.getValue())) {
          return cookie.getValue();
        }
      }
    }

    return ThemeCatalog.DEFAULT_THEME;
  }

  @ModelAttribute("currentUrl")
  public String currentUrl(HttpServletRequest request) {
    String query = request.getQueryString();
    return query == null || query.isBlank()
        ? request.getRequestURI()
        : request.getRequestURI() + "?" + query;
  }
}
