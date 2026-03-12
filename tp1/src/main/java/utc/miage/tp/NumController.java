package utc.miage.tp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class NumController {

	@GetMapping("/num/form")
	public String numForm() {
		return "num";
	}

	@PostMapping("/num/form")
	public String numSubmit(@RequestParam String number_1, @RequestParam String number_2, Model model) {
		model.addAttribute("number_1", number_1);
		model.addAttribute("number_2", number_2);
		System.out.println("Received numbers: " + number_1 + ", " + number_2);
		return "num";
	}
}
