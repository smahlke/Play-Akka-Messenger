package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import akka.actor.ActorRef;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -7922784625447104187L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "userid", nullable = false)
	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	
	@OneToOne(cascade=CascadeType.ALL)
	ActorRef actor;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "kontakte", joinColumns = { @JoinColumn(name = "user", referencedColumnName = "userid", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "contact", referencedColumnName = "userid", nullable = false) })
	private Collection<User> contactList = new HashSet<User>();

	public User() {
	}

	public User(String name) {
		this.username = name;
	}
	
	public ActorRef getActor() {
		return actor;
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<User> getContactList() {
		return contactList;
	}

	public void addToContactList(User user) {
		// Wird nur hinzugefügt, wenn dieser Nutzer diesen Kontakt noch nicht hat.
		if (!this.contactList.contains(user)) {
			if (!this.equals(user)) {
				this.contactList.add(user);
			}
		}
	}

}
