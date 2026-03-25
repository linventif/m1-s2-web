package utc.miage.tp.conference;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import utc.miage.tp.thematique.Thematique;
import utc.miage.tp.user.User;

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

	@ManyToMany
	@JoinTable(name = "traiter", joinColumns = @JoinColumn(name = "idconf"), inverseJoinColumns = @JoinColumn(name = "id_thematique"))
	private Set<Thematique> thematiques = new LinkedHashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizer_id")
	private User organizer;

	@ManyToMany(mappedBy = "participatingConferences")
	private Set<User> participants = new LinkedHashSet<>();

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

	public Set<Thematique> getThematiques() {
		return thematiques;
	}

	public void setThematiques(Set<Thematique> thematiques) {
		this.thematiques = thematiques == null ? new LinkedHashSet<>() : new LinkedHashSet<>(thematiques);
	}

	public void addThematique(Thematique thematique) {
		if (thematique != null) {
			thematiques.add(thematique);
		}
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		if (Objects.equals(this.organizer, organizer)) {
			return;
		}

		User previousOrganizer = this.organizer;
		this.organizer = organizer;

		if (previousOrganizer != null) {
			previousOrganizer.getOrganizedConferences().remove(this);
		}

		if (organizer != null) {
			organizer.getOrganizedConferences().add(this);
		}
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public void addParticipant(User user) {
		if (user == null || participants.contains(user)) {
			return;
		}
		participants.add(user);
		user.getParticipatingConferences().add(this);
	}

	public void removeParticipant(User user) {
		if (user == null || !participants.remove(user)) {
			return;
		}
		user.getParticipatingConferences().remove(this);
	}
}
