package utc.miage.tp.conference;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "conference")
public class Conference {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idconf")
	private Long idconf;

	@Column(name = "titleconf", nullable = false)
	private String titleconf;

	@Column(name = "nbeditionconf", nullable = false)
	private Integer nbeditionconf;

	@Column(name = "dtstartconf", nullable = false)
	private LocalDate dtstartconf;

	@Column(name = "dtendconf", nullable = false)
	private LocalDate dtendconf;

	@Column(name = "urlwebsiteconf")
	private String urlwebsiteconf;

	public Conference() {
	}

	public Conference(String titleconf, Integer nbeditionconf, LocalDate dtstartconf, LocalDate dtendconf,
			String urlwebsiteconf) {
		this.titleconf = titleconf;
		this.nbeditionconf = nbeditionconf;
		this.dtstartconf = dtstartconf;
		this.dtendconf = dtendconf;
		this.urlwebsiteconf = urlwebsiteconf;
	}

	public Long getIdconf() {
		return idconf;
	}

	public void setIdconf(Long idconf) {
		this.idconf = idconf;
	}

	public String getTitleconf() {
		return titleconf;
	}

	public void setTitleconf(String titleconf) {
		this.titleconf = titleconf;
	}

	public Integer getNbeditionconf() {
		return nbeditionconf;
	}

	public void setNbeditionconf(Integer nbeditionconf) {
		this.nbeditionconf = nbeditionconf;
	}

	public LocalDate getDtstartconf() {
		return dtstartconf;
	}

	public void setDtstartconf(LocalDate dtstartconf) {
		this.dtstartconf = dtstartconf;
	}

	public LocalDate getDtendconf() {
		return dtendconf;
	}

	public void setDtendconf(LocalDate dtendconf) {
		this.dtendconf = dtendconf;
	}

	public String getUrlwebsiteconf() {
		return urlwebsiteconf;
	}

	public void setUrlwebsiteconf(String urlwebsiteconf) {
		this.urlwebsiteconf = urlwebsiteconf;
	}
}
