package utc.miage.tp.conference;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

	List<Conference> findByTitleconf(String titleconf);
}
