package utc.miage.tp.tp2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String helloWorld(@RequestParam(defaultValue = "World") String name) {

		return "Hello, " + name + "!";
	}

	@GetMapping("/hello/world")
	public String hello() {
		return "Hello, World!";
	}

	@GetMapping("/hello/person/{name}")
	public String helloName(@PathVariable String name) {
		return "Hello, " + name + "!";
	}
}
