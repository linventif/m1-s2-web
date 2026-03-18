package utc.miage.tp.conference;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ConferenceService {

	private final ConferenceRepository conferenceRepository;

	public ConferenceService(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}

	public List<Conference> getAllConferences() {
		return conferenceRepository.findAll(Sort.by(Sort.Direction.ASC, "idconf"));
	}

	public List<Conference> getConferencesByTitle(String titleconf) {
		return conferenceRepository.findByTitleconf(titleconf);
	}

	public List<Conference> searchConferencesByTitle(String titleconf) {
		return conferenceRepository.findByTitleconfContainingIgnoreCase(titleconf);
	}

	public Optional<Conference> getConferenceById(Long idconf) {
		return conferenceRepository.findById(idconf);
	}

	public Conference addConference(Conference conference) {
		return conferenceRepository.save(conference);
	}

	public Optional<Conference> updateConference(Long idconf, Conference updatedConference) {
		return conferenceRepository.findById(idconf).map(existingConference -> {
			existingConference.setTitleconf(updatedConference.getTitleconf());
			existingConference.setNbeditionconf(updatedConference.getNbeditionconf());
			existingConference.setDtstartconf(updatedConference.getDtstartconf());
			existingConference.setDtendconf(updatedConference.getDtendconf());
			existingConference.setUrlwebsiteconf(updatedConference.getUrlwebsiteconf());
			return conferenceRepository.save(existingConference);
		});
	}

	public boolean deleteConference(Long idconf) {
		if (!conferenceRepository.existsById(idconf)) {
			return false;
		}
		conferenceRepository.deleteById(idconf);
		return true;
	}
}
