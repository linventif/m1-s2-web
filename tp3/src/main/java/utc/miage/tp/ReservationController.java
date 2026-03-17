package utc.miage.tp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

	private final List<Reservation> reservations = new ArrayList<>(List.of(
			new Reservation(1L, "Alice Martin", "Bretagne", "Phare et bord de mer", 2, "Carte", null),
			new Reservation(2L, "Bilal Dupont", "Alpes", "Montagnes enneigees", 4, "Virement", "FR76 3000 4000 5000 6000 7000 189"),
			new Reservation(3L, "Chloe Bernard", "Provence", "Lavande et soleil", 3, "Cheque", null),
			new Reservation(4L, "David Leroy", "Normandie", "Falaises et plage", 5, "Virement", "FR14 2004 1010 0505 0001 3M02 606")));

	@GetMapping({ "/resa", "/resa/form" })
	public String showReservations(@RequestParam(defaultValue = "false") boolean cancelled, Model model) {
		model.addAttribute("reservations", reservations);
		model.addAttribute("cancelled", cancelled);
		return "resa";
	}

	@PostMapping("/resa/{id}/cancel")
	public String cancelReservation(@PathVariable Long id) {
		reservations.removeIf(reservation -> reservation.id().equals(id));
		return "redirect:/resa?cancelled=true";
	}
}