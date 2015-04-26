package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class User extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7922784625447104187L;
	
	@Id
	private Long id;
	private String name;

	public User(String name) {
		this.name = name;
	}
	
	public User() {
		
	}
	
	public static Finder<Long, User> find = new Model.Finder<Long, User>(Long.class, User.class);
	
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
