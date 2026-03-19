package utc.miage.tp.user;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import utc.miage.tp.conference.Conference;
import utc.miage.tp.statut.Statut;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "code_statut", nullable = false)
	private Statut statut;

	@OneToMany(mappedBy = "organizer")
	private Set<Conference> organizedConferences = new LinkedHashSet<>();

	@ManyToMany
	@JoinTable(name = "participate", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "conference_id"))
	private Set<Conference> participatingConferences = new LinkedHashSet<>();

	public User() {
	}

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Statut getStatut() {
		return statut;
	}

	public void setStatut(Statut statut) {
		this.statut = statut;
	}

	public Set<Conference> getOrganizedConferences() {
		return organizedConferences;
	}

	public void setOrganizedConferences(Set<Conference> organizedConferences) {
		this.organizedConferences = organizedConferences;
	}

	public Set<Conference> getParticipatingConferences() {
		return participatingConferences;
	}

	public void setParticipatingConferences(Set<Conference> participatingConferences) {
		this.participatingConferences = participatingConferences;
	}

	public void addOrganizedConference(Conference conference) {
		if (conference == null) {
			return;
		}
		conference.setOrganizer(this);
	}

	public void addParticipatingConference(Conference conference) {
		if (conference == null || participatingConferences.contains(conference)) {
			return;
		}
		participatingConferences.add(conference);
		conference.getParticipants().add(this);
	}

	public void removeParticipatingConference(Conference conference) {
		if (conference == null || !participatingConferences.remove(conference)) {
			return;
		}
		conference.getParticipants().remove(this);
	}
}
