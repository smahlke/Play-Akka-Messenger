package models.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class UserRepository implements CrudRepository<User> {
	
	/**
	 * Repository als Singleton
	 */
	static private UserRepository instance;
	
	@Transactional
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
	public User findByUsername(String name) throws Exception {
		Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.username=:name")
                .setParameter("name", name);
		List<User> user = query.getResultList();
		if (user.size() == 0) {
			throw new Exception("Es existiert kein Benutzer mit dem Nutzernamen " + name + ".");
		}
		return user.get(0);

	}

	@Transactional
	@Override
	public void persist(User entity) {
		JPA.em().persist(entity);
	}

	@Transactional
	public static List<User> findAllUsers() {
		List<User> l = new ArrayList<User>();
		User user = new User();
		user.setUsername("testname");
		l.add(user );
		TypedQuery<User> query = (TypedQuery<User>) JPA.em().createQuery("SELECT u FROM User u", User.class);
		return query.getResultList();
		
	}
	
	public List<User> getUsersFromContactList(User user) {
		return null;
	}
	
	public void addUserToContactList(User user, User ...toAdd) {
		user.addUserToContactList(toAdd);
	}


	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
