package utc.miage.tp.user;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import utc.miage.tp.conference.Conference;
import utc.miage.tp.conference.ConferenceRepository;
import utc.miage.tp.statut.Statut;
import utc.miage.tp.statut.StatutRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final ConferenceRepository conferenceRepository;
	private final StatutRepository statutRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, ConferenceRepository conferenceRepository,
			StatutRepository statutRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.conferenceRepository = conferenceRepository;
		this.statutRepository = statutRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public List<User> getAllUser() {
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

		return users;
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsersWithOrganizedConferences() {
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		users.forEach(user -> {
			user.getOrganizedConferences().size();
			user.getParticipatingConferences().size();
		});
		return users;
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id).map(user -> {
			user.getStatut().getNomStatut();
			user.getOrganizedConferences().size();
			user.getOrganizedConferences().forEach(conference -> conference.getThematiques().size());
			user.getParticipatingConferences().size();
			user.getParticipatingConferences().forEach(conference -> conference.getThematiques().size());
			return user;
		});
	}

	@Transactional(readOnly = true)
	public List<Conference> getAllConferences() {
		List<Conference> conferences = conferenceRepository.findAll(Sort.by(Sort.Direction.ASC, "idconf"));
		conferences.forEach(conference -> conference.getThematiques().size());
		return conferences;
	}

	@Transactional(readOnly = true)
	public Optional<List<Conference>> getParticipatingConferencesByUserId(Long userId) {
		return userRepository.findById(userId).map(user -> {
			user.getParticipatingConferences().forEach(conference -> conference.getThematiques().size());
			return user.getParticipatingConferences().stream()
					.sorted(Comparator.comparing(Conference::getIdconf, Comparator.nullsLast(Long::compareTo)))
					.toList();
		});
	}

	@Transactional
	public User createUser(User user, String rawPassword, String codeStatut, Collection<Long> organizedConferenceIds,
			Collection<Long> participatingConferenceIds) {
		String normalizedEmail = normalizeEmail(user.getEmail());
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new IllegalArgumentException("Un utilisateur avec cet email existe deja.");
		}

		Statut statut = statutRepository.findById(codeStatut)
				.orElseThrow(() -> new IllegalArgumentException("Le statut selectionne est invalide."));

		User newUser = new User(user.getName(), normalizedEmail, user.getWeight(), user.getHeight());
		newUser.setPassword(passwordEncoder.encode(rawPassword));
		newUser.setStatut(statut);

		User savedUser = userRepository.save(newUser);

		for (Conference conference : getConferencesByIds(organizedConferenceIds)) {
			savedUser.addOrganizedConference(conference);
		}

		for (Conference conference : getConferencesByIds(participatingConferenceIds)) {
			savedUser.addParticipatingConference(conference);
		}

		return userRepository.save(savedUser);
	}

	@Transactional(readOnly = true)
	public List<Statut> getAllStatuts() {
		return statutRepository.findAll(Sort.by(Sort.Direction.ASC, "nomStatut"));
	}

	@Transactional(readOnly = true)
	public Optional<User> authenticate(String email, String rawPassword) {
		String normalizedEmail = normalizeEmail(email);
		return userRepository.findByEmail(normalizedEmail)
				.filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
				.map(user -> {
					user.getStatut().getNomStatut();
					user.getOrganizedConferences().size();
					user.getOrganizedConferences().forEach(conference -> conference.getThematiques().size());
					user.getParticipatingConferences().size();
					user.getParticipatingConferences().forEach(conference -> conference.getThematiques().size());
					return user;
				});
	}

	private Set<Conference> getConferencesByIds(Collection<Long> conferenceIds) {
		if (conferenceIds == null || conferenceIds.isEmpty()) {
			return Set.of();
		}
		return new LinkedHashSet<>(conferenceRepository.findAllById(conferenceIds));
	}

	private String normalizeEmail(String email) {
		if (email == null) {
			return "";
		}
		return email.trim().toLowerCase();
	}
}
