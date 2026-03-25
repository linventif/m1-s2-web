package utc.miage.tp.theme;

import java.util.List;
import java.util.Set;

public final class ThemeCatalog {

  public static final String DEFAULT_THEME = "corporate";

  public static final List<String> ALL_THEMES = List.of(
      "light",
      "dark",
      "cupcake",
      "bumblebee",
      "emerald",
      "corporate",
      "synthwave",
      "retro",
      "cyberpunk",
      "valentine",
      "halloween",
      "garden",
      "forest",
      "aqua",
      "lofi",
      "pastel",
      "fantasy",
      "wireframe",
      "black",
      "luxury",
      "dracula",
      "cmyk",
      "autumn",
      "business",
      "acid",
      "lemonade",
      "night",
      "coffee",
      "winter",
      "dim",
      "nord",
      "sunset",
      "caramellatte",
      "abyss",
      "silk");

  private static final Set<String> THEME_SET = Set.copyOf(ALL_THEMES);

  private ThemeCatalog() {
  }

  public static boolean isValid(String theme) {
    return theme != null && THEME_SET.contains(theme);
  }
}
