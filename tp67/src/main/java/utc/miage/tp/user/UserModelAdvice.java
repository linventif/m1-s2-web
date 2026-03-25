package utc.miage.tp.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserModelAdvice {

  private final UserService userService;

  public UserModelAdvice(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute("currentUser")
  public SessionUser currentUser(HttpSession session) {
    return userService.findLoggedUser(session)
        .map(user -> new SessionUser(user.getId(), user.getDisplayName(), user.getEmail()))
        .orElse(null);
  }
}
