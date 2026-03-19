package utc.miage.tp.conference;

import java.util.List;
import java.util.Optional;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import utc.miage.tp.thematique.Thematique;
import utc.miage.tp.thematique.ThematiqueRepository;

@Service
public class ConferenceService {

	private final ConferenceRepository conferenceRepository;
	private final ThematiqueRepository thematiqueRepository;

	public ConferenceService(ConferenceRepository conferenceRepository, ThematiqueRepository thematiqueRepository) {
		this.conferenceRepository = conferenceRepository;
		this.thematiqueRepository = thematiqueRepository;
	}

	@Transactional(readOnly = true)
	public List<Conference> getAllConferences() {
		List<Conference> conferences = conferenceRepository.findAll(Sort.by(Sort.Direction.ASC, "idconf"));
		conferences.forEach(conference -> conference.getThematiques().size());
		return conferences;
	}

	@Transactional(readOnly = true)
	public List<Conference> getConferencesByTitle(String titleconf) {
		List<Conference> conferences = conferenceRepository.findByTitleconf(titleconf);
		conferences.forEach(conference -> conference.getThematiques().size());
		return conferences;
	}

	@Transactional(readOnly = true)
	public List<Conference> searchConferencesByTitle(String titleconf) {
		List<Conference> conferences = conferenceRepository.findByTitleconfContainingIgnoreCase(titleconf);
		conferences.forEach(conference -> conference.getThematiques().size());
		return conferences;
	}

	@Transactional(readOnly = true)
	public Optional<Conference> getConferenceById(Long idconf) {
		return conferenceRepository.findById(idconf).map(conference -> {
			conference.getThematiques().size();
			return conference;
		});
	}

	@Transactional
	public Conference addConference(Conference conference, List<Long> thematiqueIds) {
		conference.setThematiques(getThematiquesByIds(thematiqueIds));
		return conferenceRepository.save(conference);
	}

	@Transactional
	public Optional<Conference> updateConference(Long idconf, Conference updatedConference, List<Long> thematiqueIds) {
		return conferenceRepository.findById(idconf).map(existingConference -> {
			existingConference.setTitleconf(updatedConference.getTitleconf());
			existingConference.setNbeditionconf(updatedConference.getNbeditionconf());
			existingConference.setDtstartconf(updatedConference.getDtstartconf());
			existingConference.setDtendconf(updatedConference.getDtendconf());
			existingConference.setUrlwebsiteconf(updatedConference.getUrlwebsiteconf());
			existingConference.setThematiques(getThematiquesByIds(thematiqueIds));
			return conferenceRepository.save(existingConference);
		});
	}

	@Transactional
	public boolean deleteConference(Long idconf) {
		if (!conferenceRepository.existsById(idconf)) {
			return false;
		}
		conferenceRepository.deleteById(idconf);
		return true;
	}

	@Transactional(readOnly = true)
	public List<Thematique> getAllThematiques() {
		return thematiqueRepository.findAll(Sort.by(Sort.Direction.ASC, "nomThematique"));
	}

	private Set<Thematique> getThematiquesByIds(List<Long> thematiqueIds) {
		if (thematiqueIds == null || thematiqueIds.isEmpty()) {
			return Set.of();
		}
		return new LinkedHashSet<>(thematiqueRepository.findAllById(thematiqueIds));
	}
}
