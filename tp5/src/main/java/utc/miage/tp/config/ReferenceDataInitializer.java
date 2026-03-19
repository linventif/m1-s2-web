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
import utc.miage.tp.statut.Statut;
import utc.miage.tp.statut.StatutRepository;
import utc.miage.tp.thematique.Thematique;
import utc.miage.tp.thematique.ThematiqueRepository;
import utc.miage.tp.user.User;
import utc.miage.tp.user.UserRepository;

@Component
public class ReferenceDataInitializer implements CommandLineRunner {

	private final StatutRepository statutRepository;
	private final ThematiqueRepository thematiqueRepository;
	private final ConferenceRepository conferenceRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ReferenceDataInitializer(StatutRepository statutRepository, ThematiqueRepository thematiqueRepository,
			ConferenceRepository conferenceRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.statutRepository = statutRepository;
		this.thematiqueRepository = thematiqueRepository;
		this.conferenceRepository = conferenceRepository;
		this.userRepository = userRepository;
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
				createUser("Alice Martin", "alice.martin@demo.local", statuts.get("CHERCHEUR")),
				createUser("Benoit Leroy", "benoit.leroy@demo.local", statuts.get("INDUSTRIEL")),
				createUser("Claire Dubois", "claire.dubois@demo.local", statuts.get("ETUDIANT")),
				createUser("David Morel", "david.morel@demo.local", statuts.get("CHERCHEUR")),
				createUser("Emma Petit", "emma.petit@demo.local", statuts.get("ETUDIANT")),
				createUser("Farid Haddad", "farid.haddad@demo.local", statuts.get("INDUSTRIEL")),
				createUser("Giulia Rossi", "giulia.rossi@demo.local", statuts.get("CHERCHEUR")),
				createUser("Hugo Bernard", "hugo.bernard@demo.local", statuts.get("ETUDIANT")),
				createUser("Ines Laurent", "ines.laurent@demo.local", statuts.get("ETUDIANT")),
				createUser("Jules Fontaine", "jules.fontaine@demo.local", statuts.get("INDUSTRIEL")),
				createUser("Karim Saidi", "karim.saidi@demo.local", statuts.get("CHERCHEUR")),
				createUser("Lea Garnier", "lea.garnier@demo.local", statuts.get("ETUDIANT")),
				createUser("Maya Chevalier", "maya.chevalier@demo.local", statuts.get("CHERCHEUR")),
				createUser("Nora Benali", "nora.benali@demo.local", statuts.get("INDUSTRIEL")),
				createUser("Owen Mercier", "owen.mercier@demo.local", statuts.get("ETUDIANT")));

		userRepository.saveAll(users);

		List<Conference> conferences = List.of(
				createConference("Paris AI Summit", 2026, LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12),
						"https://paris-ai.demo.local", users.get(0), thematiques, "IA", "Data Engineering"),
				createConference("DevOps Days Lyon", 9, LocalDate.of(2026, 5, 5), LocalDate.of(2026, 5, 6),
						"https://devops-lyon.demo.local", users.get(1), thematiques, "DevOps", "Cloud"),
				createConference("Cyber Future Expo", 4, LocalDate.of(2026, 5, 20), LocalDate.of(2026, 5, 22),
						"https://cyber-future.demo.local", users.get(5), thematiques, "Cybersecurite", "Cloud"),
				createConference("Open Source Connect", 12, LocalDate.of(2026, 6, 3), LocalDate.of(2026, 6, 5),
						"https://opensource-connect.demo.local", users.get(6), thematiques, "Open Source", "Web"),
				createConference("Cloud Native Toulouse", 3, LocalDate.of(2026, 6, 18), LocalDate.of(2026, 6, 19),
						"https://cloud-native.demo.local", users.get(9), thematiques, "Cloud", "DevOps"),
				createConference("Data Forge Europe", 7, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3),
						"https://data-forge.demo.local", users.get(10), thematiques, "Data Engineering", "IA"),
				createConference("Web Scale Lille", 5, LocalDate.of(2026, 8, 25), LocalDate.of(2026, 8, 26),
						"https://web-scale.demo.local", users.get(11), thematiques, "Web", "Informatique"),
				createConference("IoT Industry Forum", 2, LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 11),
						"https://iot-forum.demo.local", users.get(13), thematiques, "IoT", "Robotique"),
				createConference("Robotics Research Days", 6, LocalDate.of(2026, 10, 7), LocalDate.of(2026, 10, 9),
						"https://robotics-days.demo.local", users.get(3), thematiques, "Robotique", "IA"),
				createConference("Full Stack Nantes", 8, LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 13),
						"https://fullstack-nantes.demo.local", users.get(12), thematiques, "Web", "Open Source"));

		conferenceRepository.saveAll(conferences);

		registerParticipants(conferences.get(0), users.get(2), users.get(4), users.get(7), users.get(8), users.get(14));
		registerParticipants(conferences.get(1), users.get(0), users.get(3), users.get(6), users.get(10), users.get(13));
		registerParticipants(conferences.get(2), users.get(1), users.get(5), users.get(9), users.get(12), users.get(14));
		registerParticipants(conferences.get(3), users.get(2), users.get(6), users.get(8), users.get(11), users.get(12));
		registerParticipants(conferences.get(4), users.get(1), users.get(4), users.get(5), users.get(10), users.get(13));
		registerParticipants(conferences.get(5), users.get(0), users.get(3), users.get(7), users.get(11), users.get(14));
		registerParticipants(conferences.get(6), users.get(2), users.get(8), users.get(9), users.get(12), users.get(13));
		registerParticipants(conferences.get(7), users.get(4), users.get(5), users.get(7), users.get(10), users.get(11));
		registerParticipants(conferences.get(8), users.get(0), users.get(3), users.get(6), users.get(9), users.get(14));
		registerParticipants(conferences.get(9), users.get(1), users.get(2), users.get(8), users.get(11), users.get(12));

		userRepository.saveAll(users);
	}

	private User createUser(String name, String email, Statut statut) {
		User user = new User(name, email);
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
