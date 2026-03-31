package utc.miage.tp.config;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import utc.miage.tp.conference.Conference;
import utc.miage.tp.conference.ConferenceRepository;
import utc.miage.tp.sport.Sport;
import utc.miage.tp.sport.SportRepository;
import utc.miage.tp.statut.Statut;
import utc.miage.tp.statut.StatutRepository;
import utc.miage.tp.thematique.Thematique;
import utc.miage.tp.thematique.ThematiqueRepository;
import utc.miage.tp.user.User;
import utc.miage.tp.user.UserRepository;
import utc.miage.tp.workout.WorkoutRepository;

@Component
public class ReferenceDataInitializer implements CommandLineRunner {

	private final StatutRepository statutRepository;
	private final ThematiqueRepository thematiqueRepository;
	private final ConferenceRepository conferenceRepository;
	private final UserRepository userRepository;
	private final SportRepository sportRepository;
	private final WorkoutRepository workoutRepository;
	private final PasswordEncoder passwordEncoder;

	public ReferenceDataInitializer(StatutRepository statutRepository, ThematiqueRepository thematiqueRepository,
			ConferenceRepository conferenceRepository, UserRepository userRepository,
			SportRepository sportRepository, WorkoutRepository workoutRepository, PasswordEncoder passwordEncoder) {
		this.statutRepository = statutRepository;
		this.thematiqueRepository = thematiqueRepository;
		this.conferenceRepository = conferenceRepository;
		this.userRepository = userRepository;
		this.sportRepository = sportRepository;
		this.workoutRepository = workoutRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public void run(String... args) {
		seedReferenceData();
		seedDemoData();
	}

	private void seedReferenceData() {
		if (statutRepository.count() == 0) {
			statutRepository.saveAll(List.of(
					new Statut("ETUDIANT", "Etudiant"),
					new Statut("CHERCHEUR", "Chercheur"),
					new Statut("INDUSTRIEL", "Industriel")));
		}

		if (thematiqueRepository.count() == 0) {
			thematiqueRepository.saveAll(List.of(
					new Thematique("Informatique"),
					new Thematique("IA"),
					new Thematique("DevOps"),
					new Thematique("Cloud"),
					new Thematique("Cybersecurite"),
					new Thematique("Data Engineering"),
					new Thematique("Web"),
					new Thematique("IoT"),
					new Thematique("Robotique"),
					new Thematique("Open Source")));
		}
	}

	private void seedDemoData() {
		if (conferenceRepository.count() > 0 || userRepository.count() > 0) {
			return;
		}

		Map<String, Statut> statuts = new LinkedHashMap<>();
		for (Statut statut : statutRepository.findAll()) {
			statuts.put(statut.getCodeStatut(), statut);
		}

		Map<String, Thematique> thematiques = new LinkedHashMap<>();
		for (Thematique thematique : thematiqueRepository.findAll()) {
			thematiques.put(thematique.getNomThematique(), thematique);
		}

		List<User> users = List.of(
				createUser("Alice Martin", "alice.martin@demo.local", statuts.get("CHERCHEUR"), 65.5, 165.0, false),
				createUser("Benoit Leroy", "benoit.leroy@demo.local", statuts.get("INDUSTRIEL"), 75.5, 180.0, true),
				createUser("Owen Mercier", "owen.mercier@demo.local", statuts.get("ETUDIANT"), 85.0, 185.0, true));

		userRepository.saveAll(users);

		List<Sport> sports = List.of(
				createSport("Climbing - Speed", 3.0),
				createSport("Climbing - Booldering", 2.0),
				createSport("Running", 2.0),
				createSport("Natation - 400M", 3.0),
				createSport("Skydiving", 2.0),
				createSport("Diving", 2.0));

		sportRepository.saveAll(sports);

		List<Conference> conferences = List.of(
				createConference("Paris AI Summit", 2026, LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12),
						"https://paris-ai.demo.local", users.get(0), thematiques, "IA", "Data Engineering"),
				createConference("DevOps Days Lyon", 9, LocalDate.of(2026, 5, 5), LocalDate.of(2026, 5, 6),
						"https://devops-lyon.demo.local", users.get(1), thematiques, "DevOps", "Cloud"));

		conferenceRepository.saveAll(conferences);

		registerParticipants(conferences.get(0), users.get(1), users.get(2));
		registerParticipants(conferences.get(1), users.get(0), users.get(2));

		userRepository.saveAll(users);
	}

	private Sport createSport(String name, Double calPerMin) {
		return new Sport(name, calPerMin);
	}

	private User createUser(String name, String email, Statut statut, Double weight, Double height, Boolean sex) {
		User user = new User(name, email, weight, height, sex);
		user.setStatut(statut);
		user.setPassword(passwordEncoder.encode("demo123"));
		return user;
	}

	private Conference createConference(String title, int edition, LocalDate start, LocalDate end, String url,
			User organizer, Map<String, Thematique> thematiquesByName, String... thematiqueNames) {
		Conference conference = new Conference(title, edition, start, end, url);
		conference.setOrganizer(organizer);
		conference.setThematiques(resolveThematiques(thematiquesByName, thematiqueNames));
		return conference;
	}

	private Set<Thematique> resolveThematiques(Map<String, Thematique> thematiquesByName, String... names) {
		Set<Thematique> selected = new LinkedHashSet<>();
		for (String name : names) {
			Thematique thematique = thematiquesByName.get(name);
			if (thematique != null) {
				selected.add(thematique);
			}
		}
		return selected;
	}

	private void registerParticipants(Conference conference, User... participants) {
		for (User participant : participants) {
			conference.addParticipant(participant);
		}
	}
}
