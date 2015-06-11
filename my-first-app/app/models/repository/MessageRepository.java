package models.repository;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Callback0;
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
		Query query = JPA.em().createQuery("SELECT u FROM Message u");
		return query.getResultList();
	}

	
	
}
