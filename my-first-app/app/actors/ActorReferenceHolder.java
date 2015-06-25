package actors;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;

public class ActorReferenceHolder {

	private static ActorReferenceHolder instance;
	
	public static ActorReferenceHolder getInstance() {
		if (instance == null) {
			instance = new ActorReferenceHolder();
		}
		return instance;
	}
	
	private ActorReferenceHolder() {
		
	}
	
	private Map<String, ActorRef> refHolder = new HashMap<String, ActorRef>();
	
	public void addReference(String username, ActorRef ref) {
		this.refHolder.put(username, ref);
	}
	
	public ActorRef getReference(String username) {
		return this.refHolder.get(username);
	}
	
	public void removeReference(String username) {
		this.refHolder.remove(username);
	}
	
}
