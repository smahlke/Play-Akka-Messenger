package models.repository;

import java.util.List;

import javax.persistence.Query;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class UserRepository implements CrudRepository<User> {
	
	/**
	 * Repository als Singleton
	 */
	static private UserRepository instance;
	
	public static UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}
	
	
	/**
	 * Suche einen Benutzer mithilfe seiner ID.
	 * @param id
	 * @return
	 */
	@Transactional
	public User findById(Long id) {
		return JPA.em().find(User.class, id);
	}

	@Transactional
	@Override
	public void persist(User entity) {
		JPA.em().persist(entity);
	}

	@Override
	@Transactional
	public List<User> findAll() {
		Query query = JPA.em().createQuery("SELECT u FROM User u");
		return query.getResultList();
	}
	
	public List<User> getUsersFromContactList(User user) {
		return null;
	}
	
	public void addUserToContactList(User user, User ...toAdd) {
		user.addUserToContactList(toAdd);
	}

	
	
}
