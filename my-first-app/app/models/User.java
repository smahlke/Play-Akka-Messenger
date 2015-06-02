package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -7922784625447104187L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private String password;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn
	private List<User> contactList = new ArrayList<User>();

	public User() {
	}
	
	public User(String name) {
		this.username = name;
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
	
	public List<User> getContactList() {
		return contactList;
	}

	public void addUserToContactList(User ...users) {
		for (User a : users) {
			this.contactList.add(a);
		}
	}

}
