package models.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Callback0;
import play.libs.F.Function0;
import akka.actor.ActorRef;

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
			try {
				return JPA.withTransaction(new Function0<User>() {
					@Override
					public User apply() throws Throwable {
						TypedQuery<User> query = null;
						if (JPA.em() == null) {
							System.out.println("JPA em null");
						}
						if (JPA.em() != null) {
							System.out.println("JPA nicht null");
						}
						query = JPA
								.em()
								.createQuery(
										"SELECT u FROM User u WHERE u.username=:name",
										User.class).setParameter("name", name);
						List<User> users = query.getResultList();
						if (users.size() == 0) {
							throw new Exception(
									"Es existiert kein Benutzer mit dem Nutzernamen "
											+ name + ".");
						}
						if (users.size() > 1) {
							throw new Exception(
									"Es existiert mehrere Benutzer mit dem Nutzernamen "
											+ name + ".");
						}
						return users.get(0);
					}
				});
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Suche einen Benutzer mithilfe seiner ID.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public User findById(Long id) {
		try {
			return (User) JPA.withTransaction(new Function0<User>() {
				@Override
				public User apply() throws Throwable {
					return JPA.em().find(User.class, id);
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public void persist(User entity) {
		try {
			JPA.withTransaction(new Callback0() {

				@Override
				public void invoke() throws Throwable {
					JPA.em().persist(entity);
				}
			});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Transactional
	public void addUserToContactList(String username, Long contactid) {
		User user = UserRepository.getInstance().findByUsername(username);
		User contact = UserRepository.getInstance().findById(contactid);
		
		JPA.withTransaction(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				User u = JPA.em().find(User.class, user.getId());
				User c = JPA.em().find(User.class, contact.getId());
				u.addToContactList(c);
				

			}
		});
	}
	
	@Transactional
	public void setActorRefAtUser(User user, ActorRef ref) {
		JPA.withTransaction(new Callback0() {
			@Override
			public void invoke() throws Throwable {
				User u = JPA.em().find(User.class, user.getId());
				u.setActor(ref);
			}
		});
	}
	
	@Transactional
	public List<User> findAll() {
		try {
			return (List<User>) JPA
					.withTransaction(new Function0<List<User>>() {
						@Override
						public List<User> apply() throws Throwable {
							TypedQuery<User> query = null;
							query = (TypedQuery<User>) JPA.em().createQuery(
									"SELECT u FROM User u", User.class);
							List<User> users = query.getResultList();

							return users;
						}
					});
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
