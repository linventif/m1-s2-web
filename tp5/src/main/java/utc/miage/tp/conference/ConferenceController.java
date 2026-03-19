package utc.miage.tp.conference;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

	private final ConferenceService conferenceService;

	public ConferenceController(ConferenceService conferenceService) {
		this.conferenceService = conferenceService;
	}

	@GetMapping({ "", "/" })
	public String showMenu() {
		return "conference-menu";
	}

	@GetMapping("/list")
	public String showAllConferences(Model model) {
		model.addAttribute("message", "Liste complete des conferences.");
		model.addAttribute("conferences", conferenceService.getAllConferences());
		return "conference-list";
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("conference", new Conference());
		model.addAttribute("thematiques", conferenceService.getAllThematiques());
		return "conference-create";
	}

	@PostMapping("/create")
	public String createConference(@ModelAttribute Conference conference,
			@RequestParam(name = "thematiqueIds", required = false) List<Long> thematiqueIds, Model model) {
		Conference createdConference = conferenceService.addConference(conference, thematiqueIds);
		model.addAttribute("message", "Conference ajoutee avec succes : " + createdConference.getTitleconf() + ".");
		model.addAttribute("conferences", conferenceService.getAllConferences());
		return "conference-list";
	}

	@GetMapping("/read")
	public String showReadForm() {
		return "conference-read";
	}

	@PostMapping("/read")
	public String readConferences(@RequestParam String titleconf, Model model) {
		model.addAttribute("message", "Recherche terminee pour le titre : " + titleconf + ".");
		model.addAttribute("searchResults", conferenceService.searchConferencesByTitle(titleconf));
		model.addAttribute("searchedTitle", titleconf);
		model.addAttribute("conferences", conferenceService.getAllConferences());
		return "conference-list";
	}

	@GetMapping("/update")
	public String showUpdateForm(Model model) {
		model.addAttribute("conference", new Conference());
		model.addAttribute("thematiques", conferenceService.getAllThematiques());
		return "conference-update";
	}

	@PostMapping("/update")
	public String updateConference(@RequestParam Long idconf, @ModelAttribute Conference conference,
			@RequestParam(name = "thematiqueIds", required = false) List<Long> thematiqueIds, Model model) {
		return conferenceService.updateConference(idconf, conference, thematiqueIds)
				.map(updatedConference -> {
					model.addAttribute("message",
							"Conference mise a jour avec succes : " + updatedConference.getTitleconf() + ".");
					model.addAttribute("conferences", conferenceService.getAllConferences());
					return "conference-list";
				})
				.orElseGet(() -> {
					model.addAttribute("message", "Aucune conference trouvee avec l'identifiant " + idconf + ".");
					model.addAttribute("conferences", conferenceService.getAllConferences());
					return "conference-list";
				});
	}

	@GetMapping("/delete")
	public String showDeleteForm() {
		return "conference-delete";
	}

	@PostMapping("/delete")
	public String deleteConference(@RequestParam Long idconf, Model model) {
		boolean deleted = conferenceService.deleteConference(idconf);
		if (deleted) {
			model.addAttribute("message", "Conference supprimee avec succes : id " + idconf + ".");
		} else {
			model.addAttribute("message", "Aucune conference trouvee avec l'identifiant " + idconf + ".");
		}
		model.addAttribute("conferences", conferenceService.getAllConferences());
		return "conference-list";
	}
}
