package utc.miage.tp.statut;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "statuts")
public class Statut {

	@Id
	@Column(name = "code_statut", nullable = false, length = 64)
	private String codeStatut;

	@Column(name = "nom_statut", nullable = false, unique = true)
	private String nomStatut;

	public Statut() {
	}

	public Statut(String codeStatut, String nomStatut) {
		this.codeStatut = codeStatut;
		this.nomStatut = nomStatut;
	}

	public String getCodeStatut() {
		return codeStatut;
	}

	public void setCodeStatut(String codeStatut) {
		this.codeStatut = codeStatut;
	}

	public String getNomStatut() {
		return nomStatut;
	}

	public void setNomStatut(String nomStatut) {
		this.nomStatut = nomStatut;
	}
}
