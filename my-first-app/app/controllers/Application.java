package controllers;

import models.Message;
import models.User;
import models.repository.MessageRepository;
import models.repository.UserRepository;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.index;
import views.html.main;
import actors.ChatRoomActor;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import static akka.pattern.Patterns.ask;

public class Application extends Controller {

	static ActorSystem actorSystem = ActorSystem.create( "play" );
    
    static {
         // Create our local actors
    	User anton = new User("Anton");
    	User sebastian = new User("Sebastian");
    	anton.addUserToContactList(sebastian);
    	sebastian.addUserToContactList(anton);
    	UserRepository.getInstance().persist(sebastian);
    	UserRepository.getInstance().persist(anton);
    	
         actorSystem.actorOf( ChatRoomActor.props(anton, sebastian),  anton.getName()+sebastian.getName()+"ChatRoomActor" );
    }
   
   public static Result index() {
       return ok(index.render("Your new application is ready."));
   }
  
   /**
    * Controller action that constructs a MyMessage and sends it to our local
    * Hello, World actor
    *
    * @param name          The name of the person to greet
    * @return               The promise of a Result
    */
   @Transactional
   public static Promise<Result> localHello( String messageString)
   {
	   
	   Message message = new Message(messageString);
	   message.setSource(new User("Anton"));
	   message.setDestination(new User("Sebastian"));
//	   MessageRepository.getInstance().persist(message);
        // Look up the actor
        ActorSelection myActor =
                  actorSystem.actorSelection( "user/" + message.getSource().getName() + message.getDestination().getName() + "ChatRoomActor" );
       
       
        // As the actor for a response to the message (and a 30 second timeout);
        // ask returns an Akka Future, so we wrap it with a Play Promise
        return Promise.wrap(ask(myActor, message, 30000)).map(
                    new Function<Object, Result>() {
                        public Result apply(Object response) {
                             if( response instanceof Message ) {
                            	 Message message = ( Message )response;
                                  return ok( message.getMessage() );
                             }
                            return notFound( "Message is not of type MyMessage" );
                        }
                    }
                );
   }
	
    
    @Transactional
    public static Result main() {
    	
    	User u = new User();
    	u.setName("jiiiiipiii");
    	JPA.em().persist(u);
    	
    	Message m = new Message();
    	m.setMessage("some message");
    	m.setSource(u);
    	m.setDestination(u);
    	JPA.em().persist(m);
    	
        return ok(main.render("Some Title", Html.apply("<span> bla </span>")));
    }
    
    @Transactional
    public static Result trivial(String name) {
        return ok(main.render("Title", Html.apply(""+MessageRepository.getInstance().findAll() +"")));
    }
    
    public static Result test(String name) {
    	return ok("Hello " + name);
        
    }
}
