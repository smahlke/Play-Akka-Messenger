package models.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;

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
	
	@Transactional
	public User findByUsername(final String name) {
	    try {
	        return JPA.withTransaction(new play.libs.F.Function0<User>() {
	            public User apply() throws Exception {       
	    			TypedQuery<User> query = null;
	    			query = JPA.em().createQuery("SELECT u FROM User u WHERE u.username=:name", User.class)
			                .setParameter("name", name);
	    			List<User> users = query.getResultList();
	    			if (users.size() == 0) {
	    				throw new Exception("Es existiert kein Benutzer mit dem Nutzernamen " + name + ".");
	    			}
	    			
					return users.get(0);
	            }
	        });
	    } catch (Throwable t) {
	        throw new RuntimeException(t);
	    }       
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

	@Transactional
	public List<User> findAllUsers() {
		try {
	        return JPA.withTransaction(new play.libs.F.Function0<List<User>>() {
	            public List<User> apply() throws Exception {       
	    			TypedQuery<User> query = null;
	    			query = (TypedQuery<User>) JPA.em().createQuery("SELECT u FROM User u", User.class);
	    			List<User> users = query.getResultList();
	    			
					return users;
	            }
	        });
	    } catch (Throwable t) {
	        throw new RuntimeException(t);
	    }       
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
