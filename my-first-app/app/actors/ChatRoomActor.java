package actors;

import models.Message;
import models.User;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

public class ChatRoomActor extends UntypedActor {

	private User a;
	private User b;
	
	public ChatRoomActor(User a, User b) {
		this.a = a;
		this.b = b;
	}
	
	public static Props props(final User a, final User b) {
	    return Props.create(new Creator<ChatRoomActor>() {
	      private static final long serialVersionUID = 1L;
	 
	      @Override
	      public ChatRoomActor create() throws Exception {
	        return new ChatRoomActor(a, b);
	      }
	    });
	  }
	
	@Override
	public void onReceive(Object message) throws Exception {
		if( message instanceof Message )
        {
			Message myMessage = ( Message )message;
             myMessage.setMessage(myMessage.getSource().getUsername() + " says: " + myMessage.getMessage() + " to " + myMessage.getDestination().getUsername());
             getSender().tell( myMessage, getSelf() );
        }
        else
        {
             unhandled( message );
        }		
	}

	
	
}
