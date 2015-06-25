package actors;

import java.io.IOException;
import java.util.Date;

import models.Message;
import models.User;
import models.repository.MessageRepository;
import models.repository.UserRepository;
import play.libs.Json;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatRoomActor extends UntypedActor {

	public static Props props(ActorRef out) {
		return Props.create(ChatRoomActor.class, out);
	}

	private final ActorRef out;

	public ChatRoomActor(ActorRef out) {
		this.out = out;
	}

	// @Override
	// public void preStart() {
	// out.tell("You are connected.", getSelf());
	// }

	public Message translateToMessage(String string)
			throws JsonProcessingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(string);

		Message m = new Message();
		User destinationUser = UserRepository.getInstance().findByUsername(
				jsonNode.get("destination").asText());
		m.setDestination(destinationUser);
		m.setSource(UserRepository.getInstance().findByUsername(
				jsonNode.get("source").asText()));
		m.setMessage(jsonNode.get("message").asText());
		m.setTimestamp(new Date());
		return m;
	}

	public ObjectNode translateToObjectNode(Message m) {
		// generate json output
		ObjectNode result = Json.newObject();
		result.put("message", m.getMessage());
		result.put("source", m.getSource().getUsername());
		result.put("destination", m.getDestination().getUsername());
		result.put("timestamp", m.getTimestamp().toString());
		return result;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {

			Message m = this.translateToMessage((String) message);

			ObjectNode result = this.translateToObjectNode(m);
			
			ActorRef dest = ActorReferenceHolder.getInstance().getReference(m.getDestination().getUsername());
			if (dest != null) {
				dest.tell(result.toString(), getSelf());
				//sollte von Client bestätigt werden
				m.setReceivedOnClient(true);
			} else {
				m.setReceivedOnClient(false);
			}
			MessageRepository.getInstance().persist(m);
		} else {
			unhandled(message);
		}
	}
}
