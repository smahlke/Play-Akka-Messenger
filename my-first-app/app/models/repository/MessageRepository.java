package models.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.Message;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Callback0;
import play.libs.F.Function0;

public class MessageRepository implements CrudRepository<Message> {

	/**
	 * Repository als Singleton
	 */
	static private MessageRepository instance;

	public static MessageRepository getInstance() {
		if (instance == null) {
			instance = new MessageRepository();
		}
		return instance;
	}

	/**
	 * Suche eine Nachricht mithilfe der ID.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public Message findById(Long id) {
		return JPA.em().find(Message.class, id);
	}

	@Transactional
	@Override
	public void persist(Message entity) {
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

	@Override
	@Transactional
	public List<Message> findAll() {
		List<Message> list = new ArrayList<Message>();
		try {
			JPA.withTransaction(new Callback0() {
				@Override
				public void invoke() throws Throwable {
					Query query = JPA.em().createQuery(
							"SELECT u FROM Message u");
					list.addAll(query.getResultList());
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Transactional
	public List<Message> findAllUnreadMessages(String username) {
		List<Message> list = new ArrayList<Message>();
		try {
			JPA.withTransaction(new Callback0() {
				@Override
				public void invoke() throws Throwable {
					Query query = JPA.em().createQuery(
							"SELECT m FROM Message m, User u WHERE m.source = u.id AND u.username=:name AND receivedonclient = 0").setParameter("name", username);
					list.addAll(query.getResultList());
					//TODO: Mark as read
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return list;
	}
}
