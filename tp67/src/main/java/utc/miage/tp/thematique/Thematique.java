package utc.miage.tp.thematique;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "thematiques")
public class Thematique {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_thematique")
	private Long idThematique;

	@Column(name = "nom_thematique", nullable = false, unique = true)
	private String nomThematique;

	public Thematique() {
	}

	public Thematique(String nomThematique) {
		this.nomThematique = nomThematique;
	}

	public Long getIdThematique() {
		return idThematique;
	}

	public void setIdThematique(Long idThematique) {
		this.idThematique = idThematique;
	}

	public String getNomThematique() {
		return nomThematique;
	}

	public void setNomThematique(String nomThematique) {
		this.nomThematique = nomThematique;
	}
}
