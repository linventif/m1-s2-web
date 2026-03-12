package utc.miage.tp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class FormController {

	@GetMapping("/hello/form")
	public String helloForm() {
		return "hello";
	}

	@PostMapping("/hello/form")
	public String helloSubmit(@RequestParam String name, Model model) {
		model.addAttribute("name", name);
		System.out.println("Received name: " + name);
		return "hello";
	}
}
