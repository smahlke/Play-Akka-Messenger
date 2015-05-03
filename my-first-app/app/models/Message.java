package models;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

import play.db.jpa.JPA;


@Entity
public class Message {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@NotNull
	private String message;

	@NotNull
	@OneToOne
	private User source;

	@NotNull
	@OneToOne
	private User destination;
	
	private boolean receivedOnServer; // empfangen
	private boolean receivedOnClient; // gelesen

	public Message() {
		this.receivedOnClient = false;
		this.receivedOnServer = false;
	}

	public Message(String message, User source, User destination) {
		this.source = source;
		this.destination = destination;
		this.message = message;
		this.receivedOnClient = false;
		this.receivedOnServer = false;
	}

	public static Message findById(Long id) {
		return JPA.em().find(Message.class, id);
	}

	public static List<Message> findAllMessages() {
		Query query = JPA.em().createQuery("SELECT m FROM Message m");
		return query.getResultList();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getSource() {
		return source;
	}

	public void setSource(User source) {
		this.source = source;
	}

	public User getDestination() {
		return destination;
	}

	public void setDestination(User destination) {
		this.destination = destination;
	}
	
	public boolean isReceivedOnServer() {
		return receivedOnServer;
	}

	public void setReceivedOnServer(boolean receivedOnServer) {
		this.receivedOnServer = receivedOnServer;
	}

	public boolean isReceivedOnClient() {
		return receivedOnClient;
	}

	public void setReceivedOnClient(boolean receivedOnClient) {
		this.receivedOnClient = receivedOnClient;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", message=" + message + ", source="
				+ source + ", destination=" + destination
				+ ", receivedOnServer=" + receivedOnServer
				+ ", receivedOnClient=" + receivedOnClient + "]";
	}


}
