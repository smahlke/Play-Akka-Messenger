package actors;

import models.Message;
import models.User;
import models.repository.UserRepository;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.Application;

public class ChatRoomActor extends UntypedActor {

	// private User a;
	// private User b;
	//
	// public ChatRoomActor(User a, User b) {
	// this.a = a;
	// this.b = b;
	// }

	public static Props props(ActorRef out) {
		return Props.create(ChatRoomActor.class, out);
	}

	private final ActorRef out;

	// private final ActorRef router;

	public ChatRoomActor(ActorRef out) {
		this.out = out;
		// this.router= getContext().actorSelection("");
	}

	// @Override
	// public void preStart() {
	// router.tell(new Message(), getSelf());
	// }

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {

		String jsonMessage = (String)message;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(jsonMessage);
			Message m = new Message();

			User destinationUser = UserRepository.getInstance().findByUsername(
					jsonNode.get("destination").asText());
			m.setDestination(destinationUser);
			m.setSource(UserRepository.getInstance().findByUsername(
					jsonNode.get("source").asText()));
			m.setMessage(jsonNode.get("text").asText());
			
			Application.actorSystem.actorFor("");
			// Message sichern
			if (destinationUser.getActor() != null) {
				destinationUser.getActor().tell(m.toString(), getSelf());
			} else {
				//save in Postbox of destinationUser
			}
			// out.tell(message, getSelf());
		} else {
			unhandled(message);
		}
	} // public static Props props(final User a, final User b) {
		// return Props.create(new Creator<ChatRoomActor>() {
		// private static final long serialVersionUID = 1L;
		//
		// @Override
		// public ChatRoomActor create() throws Exception {
		// return new ChatRoomActor(a, b);
		// }
		// });
		// }
		//
		// @Override
		// public void onReceive(Object message) throws Exception {
		// if( message instanceof Message )
		// {
		// Message myMessage = ( Message )message;
		// myMessage.setMessage(myMessage.getSource().getUsername() + " says: "
		// +
		// myMessage.getMessage() + " to " +
		// myMessage.getDestination().getUsername());
		// getSender().tell( myMessage, getSelf() );
		// }
		// else
		// {
		// unhandled( message );
		// }
		// }

}
