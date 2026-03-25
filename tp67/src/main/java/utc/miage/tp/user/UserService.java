package utc.miage.tp.user;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  public static final String SESSION_USER_ID = "userId";

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public UserService(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  public AppUser register(String displayName, String email, String password) {
    String normalizedEmail = normalizeEmail(email);
    if (appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
      throw new IllegalArgumentException("Un compte existe deja avec cet email.");
    }

    AppUser user = new AppUser(displayName.trim(), normalizedEmail, passwordEncoder.encode(password));
    return appUserRepository.save(user);
  }

  public AppUser authenticate(String email, String password) {
    String normalizedEmail = normalizeEmail(email);
    AppUser user = appUserRepository.findByEmailIgnoreCase(normalizedEmail)
        .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe invalide."));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new IllegalArgumentException("Email ou mot de passe invalide.");
    }

    return user;
  }

  public void login(HttpSession session, AppUser user) {
    session.setAttribute(SESSION_USER_ID, user.getId());
  }

  public void logout(HttpSession session) {
    session.invalidate();
  }

  public Optional<AppUser> findLoggedUser(HttpSession session) {
    Object value = session.getAttribute(SESSION_USER_ID);
    if (!(value instanceof Long userId)) {
      return Optional.empty();
    }

    return appUserRepository.findById(userId);
  }

  private String normalizeEmail(String email) {
    return email == null ? "" : email.trim().toLowerCase();
  }
}
