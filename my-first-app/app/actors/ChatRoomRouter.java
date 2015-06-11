package actors;

import models.Message;
import models.User;
import models.repository.UserRepository;
import akka.actor.UntypedActor;

public class ChatRoomRouter extends UntypedActor {
	//
	// private final Set<ActorRef> senders = new HashSet<>();
	//
	// private void addSender(ActorRef actorRef){
	// senders.add(actorRef);
	// }
	//
	// private void removeSender(ActorRef actorRef){
	// senders.remove(actorRef);
	// }

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Message) {
			User destination = UserRepository.getInstance().findByUsername(
					((Message) message).getDestination().getUsername());
			if (destination.getActor() == null) {
				// Nutzer hat momentan keinen Actor; also hat keinen ChatRoom offen
				// Nachricht in seiner Postbox speichern
			} else {
				destination.getActor().tell(message, getSelf());
			}

			//
			// // addSender(sender);
			// // getContext().watch(sender); // Watch sender so we can detect
			// when they die.
			// } else if (message instanceof Terminated) {
			// // One of our watched senders has died.
			// removeSender(sender);
			// } else if (message instanceof String) {
			// for (ActorRef sender : senders) {
			// sender.tell(message, getSelf());
			// }
			// }
		}
	}

}
