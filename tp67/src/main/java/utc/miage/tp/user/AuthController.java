package utc.miage.tp.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String showLogin(Model model) {
    model.addAttribute("pageTitle", "Connexion");
    return "auth/login";
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam(defaultValue = "/films/list?page=1") String redirect,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      AppUser user = userService.authenticate(email, password);
      userService.login(session, user);
      redirectAttributes.addFlashAttribute("feedbackMessage", "Connexion reussie.");
      redirectAttributes.addFlashAttribute("feedbackType", "success");
      return safeRedirect(redirect);
    } catch (IllegalArgumentException exception) {
      redirectAttributes.addFlashAttribute("feedbackMessage", exception.getMessage());
      redirectAttributes.addFlashAttribute("feedbackType", "error");
      redirectAttributes.addFlashAttribute("email", email);
      return "redirect:/auth/login";
    }
  }

  @GetMapping("/signup")
  public String showSignup(Model model) {
    model.addAttribute("pageTitle", "Inscription");
    return "auth/signup";
  }

  @PostMapping("/signup")
  public String signup(
      @RequestParam String displayName,
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam(defaultValue = "/films/list?page=1") String redirect,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      AppUser user = userService.register(displayName, email, password);
      userService.login(session, user);
      redirectAttributes.addFlashAttribute("feedbackMessage", "Compte cree avec succes.");
      redirectAttributes.addFlashAttribute("feedbackType", "success");
      return safeRedirect(redirect);
    } catch (IllegalArgumentException exception) {
      redirectAttributes.addFlashAttribute("feedbackMessage", exception.getMessage());
      redirectAttributes.addFlashAttribute("feedbackType", "error");
      redirectAttributes.addFlashAttribute("displayName", displayName);
      redirectAttributes.addFlashAttribute("email", email);
      return "redirect:/auth/signup";
    }
  }

  @PostMapping("/logout")
  public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
    userService.logout(session);
    redirectAttributes.addFlashAttribute("feedbackMessage", "Deconnexion reussie.");
    redirectAttributes.addFlashAttribute("feedbackType", "info");
    return "redirect:/films/list?page=1";
  }

  private String safeRedirect(String redirect) {
    if (redirect == null || redirect.isBlank() || !redirect.startsWith("/")) {
      return "redirect:/films/list?page=1";
    }
    return "redirect:" + redirect;
  }
}
