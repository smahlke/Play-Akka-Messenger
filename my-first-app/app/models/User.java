package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = -7922784625447104187L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String name;
	
	@OneToMany
	//@JoinColumn
	private List<User> contactList;

	public User(String name) {
		this.name = name;
		this.contactList = new ArrayList<User>();
	}

	public User() {
		this.contactList = new ArrayList<User>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
