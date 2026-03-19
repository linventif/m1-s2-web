package utc.miage.tp.user;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utc.miage.tp.conference.Conference;
import utc.miage.tp.conference.ConferenceRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final ConferenceRepository conferenceRepository;

	public UserService(UserRepository userRepository, ConferenceRepository conferenceRepository) {
		this.userRepository = userRepository;
		this.conferenceRepository = conferenceRepository;
	}

	@Transactional(readOnly = true)
	public List<User> getAllUsersWithOrganizedConferences() {
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		users.forEach(user -> user.getOrganizedConferences().size());
		return users;
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id).map(user -> {
			user.getOrganizedConferences().size();
			user.getParticipatingConferences().size();
			return user;
		});
	}

	@Transactional(readOnly = true)
	public List<Conference> getAllConferences() {
		return conferenceRepository.findAll(Sort.by(Sort.Direction.ASC, "idconf"));
	}

	@Transactional(readOnly = true)
	public Optional<List<Conference>> getParticipatingConferencesByUserId(Long userId) {
		return userRepository.findById(userId).map(user -> user.getParticipatingConferences().stream()
				.sorted(Comparator.comparing(Conference::getIdconf, Comparator.nullsLast(Long::compareTo)))
				.toList());
	}

	@Transactional
	public User createUser(User user, Collection<Long> organizedConferenceIds, Collection<Long> participatingConferenceIds) {
		User savedUser = userRepository.save(new User(user.getName(), user.getEmail()));

		for (Conference conference : getConferencesByIds(organizedConferenceIds)) {
			savedUser.addOrganizedConference(conference);
		}

		for (Conference conference : getConferencesByIds(participatingConferenceIds)) {
			savedUser.addParticipatingConference(conference);
		}

		return userRepository.save(savedUser);
	}

	private Set<Conference> getConferencesByIds(Collection<Long> conferenceIds) {
		if (conferenceIds == null || conferenceIds.isEmpty()) {
			return Set.of();
		}
		return new LinkedHashSet<>(conferenceRepository.findAllById(conferenceIds));
	}
}
