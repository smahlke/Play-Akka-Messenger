package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.jpa.*;

@Entity
public class User {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7922784625447104187L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String name;

	public User(String name) {
		this.name = name;
	}

	public User() {

	}

	public static User findById(Long id) {
		return JPA.em().find(User.class, id);
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

}
