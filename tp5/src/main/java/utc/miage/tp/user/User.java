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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import utc.miage.tp.conference.Conference;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "email", nullable = false)
	private String email;

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
