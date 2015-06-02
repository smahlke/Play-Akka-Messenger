package models.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import models.User;
import be.objectify.as.AsyncJPA;
import be.objectify.as.AsyncTransactional;

public class UserRepository implements CrudRepository<User> {

	/**
	 * Repository als Singleton
	 */
	static private UserRepository instance;

	@AsyncTransactional
	public static UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}
	
	@AsyncTransactional
	public User findByUsername(final String name) {
		try {
			TypedQuery<User> query = null;
			query = AsyncJPA.em()
					.createQuery(
							"SELECT u FROM User u WHERE u.username=:name",
							User.class).setParameter("name", name);
			List<User> users = query.getResultList();
			if (users.size() == 0) {
				throw new Exception(
						"Es existiert kein Benutzer mit dem Nutzernamen "
								+ name + ".");
			}

			return users.get(0);
		}
		catch(Exception e) {
			
		}
		return null;
	}

	/**
	 * Suche einen Benutzer mithilfe seiner ID.
	 * 
	 * @param id
	 * @return
	 */
	@AsyncTransactional
	public User findById(Long id) {
		return AsyncJPA.em().find(User.class, id);
	}

	@AsyncTransactional
	public void persist(User entity) {
		AsyncJPA.em().persist(entity);
	}
	
	@AsyncTransactional
	public List<User> findAllUsers() {
		TypedQuery<User> query = null;
		query = (TypedQuery<User>) AsyncJPA.em().createQuery(
				"SELECT u FROM User u", User.class);
		List<User> users = query.getResultList();

		return users;
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
