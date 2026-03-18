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
	public String numSubmit(@RequestParam("number_1") int number1, @RequestParam("number_2") int number2, Model model) {
		int min = Math.min(number1, number2);
		int max = Math.max(number1, number2);
		model.addAttribute("number_1", min);
		model.addAttribute("number_2", max);
		return "num";
	}
}
