package models.repository;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import models.Message;

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
		JPA.em().persist(entity);
	}

	@Override
	@Transactional
	public List<Message> findAll() {
		Query query = JPA.em().createQuery("SELECT u FROM Message u");
		return query.getResultList();
	}

	
	
}
