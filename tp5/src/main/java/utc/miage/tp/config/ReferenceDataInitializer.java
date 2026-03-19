package utc.miage.tp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import utc.miage.tp.statut.Statut;
import utc.miage.tp.statut.StatutRepository;
import utc.miage.tp.thematique.Thematique;
import utc.miage.tp.thematique.ThematiqueRepository;

@Configuration
public class ReferenceDataInitializer {

	@Bean
	CommandLineRunner initializeReferenceData(StatutRepository statutRepository,
			ThematiqueRepository thematiqueRepository) {
		return args -> {
			if (statutRepository.count() == 0) {
				statutRepository.save(new Statut("ETUDIANT", "Etudiant"));
				statutRepository.save(new Statut("CHERCHEUR", "Chercheur"));
				statutRepository.save(new Statut("INDUSTRIEL", "Industriel"));
			}

			if (thematiqueRepository.count() == 0) {
				thematiqueRepository.save(new Thematique("Informatique"));
				thematiqueRepository.save(new Thematique("IA"));
				thematiqueRepository.save(new Thematique("DevOps"));
			}
		};
	}
}
