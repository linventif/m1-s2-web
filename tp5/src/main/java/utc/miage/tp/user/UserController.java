package utc.miage.tp.user;

import java.util.Comparator;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import utc.miage.tp.conference.Conference;

@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping({ "", "/" })
	public String showMenu() {
		return "user-menu";
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		populateUserCreationForm(model, new User());
		return "user-create";
	}

	@PostMapping("/create")
	public String createUser(@ModelAttribute User user,
			@RequestParam String password,
			@RequestParam String codeStatut,
			@RequestParam(name = "organizedConferenceIds", required = false) List<Long> organizedConferenceIds,
			@RequestParam(name = "participatingConferenceIds", required = false) List<Long> participatingConferenceIds,
			Model model) {
		try {
			User createdUser = userService.createUser(user, password, codeStatut, organizedConferenceIds,
					participatingConferenceIds);
			model.addAttribute("message", "Utilisateur ajoute avec succes : " + createdUser.getName() + ".");
			model.addAttribute("users", userService.getAllUsersWithOrganizedConferences());
			return "user-list";
		} catch (IllegalArgumentException exception) {
			populateUserCreationForm(model, user);
			model.addAttribute("errorMessage", exception.getMessage());
			return "user-create";
		}
	}

	@GetMapping("/list")
	public String showUsers(Model model) {
		model.addAttribute("message", "Liste complete des utilisateurs et des conferences qu'ils organisent.");
		model.addAttribute("users", userService.getAllUsersWithOrganizedConferences());
		return "user-list";
	}

	@GetMapping("/participations")
	public String showParticipationsForm() {
		return "user-participations-form";
	}

	@PostMapping("/participations")
	public String showParticipations(@RequestParam Long id, Model model) {
		return userService.getUserById(id).map(user -> {
			model.addAttribute("message", "Liste des conferences auxquelles " + user.getName() + " participe.");
			model.addAttribute("user", user);
			model.addAttribute("conferences", userService.getParticipatingConferencesByUserId(id).orElse(List.of()));
			return "user-participations";
		}).orElseGet(() -> {
			model.addAttribute("message", "Aucun utilisateur trouve avec l'identifiant " + id + ".");
			model.addAttribute("searchedUserId", id);
			model.addAttribute("conferences", List.of());
			return "user-participations";
		});
	}

	@GetMapping("/login")
	public String showLoginForm(@RequestParam(name = "message", required = false) String message, Model model) {
		model.addAttribute("message", message);
		return "user-login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		return userService.authenticate(email, password)
				.map(user -> {
					session.setAttribute("loggedUserId", user.getId());
					return "redirect:/users/me";
				})
				.orElseGet(() -> {
					model.addAttribute("errorMessage", "Email ou mot de passe incorrect.");
					model.addAttribute("message", "Connectez-vous avec vos identifiants.");
					return "user-login";
				});
	}

	@GetMapping("/me")
	public String showLoggedUserDashboard(HttpSession session, Model model) {
		Object loggedUserId = session.getAttribute("loggedUserId");
		if (!(loggedUserId instanceof Long userId)) {
			return "redirect:/users/login";
		}

		return userService.getUserById(userId)
				.map(user -> {
					model.addAttribute("user", user);
					model.addAttribute("organizedConferences",
							user.getOrganizedConferences().stream()
									.sorted(Comparator.comparing(Conference::getIdconf, Comparator.nullsLast(Long::compareTo)))
									.toList());
					model.addAttribute("participatingConferences",
							user.getParticipatingConferences().stream().sorted(
									Comparator.comparing(Conference::getIdconf, Comparator.nullsLast(Long::compareTo)))
									.toList());
					return "user-dashboard";
				})
				.orElseGet(() -> {
					session.invalidate();
					return "redirect:/users/login";
				});
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/users/login?message=Session fermee.";
	}

	private void populateUserCreationForm(Model model, User user) {
		model.addAttribute("user", user);
		model.addAttribute("conferences", userService.getAllConferences());
		model.addAttribute("statuts", userService.getAllStatuts());
	}
}
