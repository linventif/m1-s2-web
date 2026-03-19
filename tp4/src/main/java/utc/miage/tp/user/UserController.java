package utc.miage.tp.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		model.addAttribute("user", new User());
		model.addAttribute("conferences", userService.getAllConferences());
		return "user-create";
	}

	@PostMapping("/create")
	public String createUser(@ModelAttribute User user,
			@RequestParam(name = "organizedConferenceIds", required = false) List<Long> organizedConferenceIds,
			@RequestParam(name = "participatingConferenceIds", required = false) List<Long> participatingConferenceIds,
			Model model) {
		User createdUser = userService.createUser(user, organizedConferenceIds, participatingConferenceIds);
		model.addAttribute("message", "Utilisateur ajoute avec succes : " + createdUser.getName() + ".");
		model.addAttribute("users", userService.getAllUsersWithOrganizedConferences());
		return "user-list";
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
}
