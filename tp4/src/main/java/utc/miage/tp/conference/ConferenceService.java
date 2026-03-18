package utc.miage.tp.conference;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ConferenceService {

	private final ConferenceRepository conferenceRepository;

	public ConferenceService(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}

	public List<Conference> getAllConferences() {
		return conferenceRepository.findAll();
	}

	public List<Conference> getConferencesByTitle(String titleconf) {
		return conferenceRepository.findByTitleconf(titleconf);
	}

	public Conference addConference(Conference conference) {
		return conferenceRepository.save(conference);
	}
}
