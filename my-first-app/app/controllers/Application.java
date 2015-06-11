package controllers;

import static akka.pattern.Patterns.ask;

import java.util.List;

import models.ActorReferenceHolder;
import models.Message;
import models.User;
import models.repository.MessageRepository;
import models.repository.UserRepository;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.WebSocket;
import play.mvc.With;
import play.twirl.api.Html;
import actors.ChatRoomActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@With(Users.class)
public class Application extends Controller {

	final static Form<User> userForm = Form.form(User.class);

	public static ActorSystem actorSystem = ActorSystem.create("play");

	static {
		// Create our local actors
		// User anton = new User("Anton");
		// User sebastian = new User("Sebastian");
		// anton.addUserToContactList(sebastian);
		// sebastian.addUserToContactList(anton);
		// UserRepository.getInstance().persist(sebastian);
		// UserRepository.getInstance().persist(anton);
		//
		// actorSystem.actorOf(ChatRoomActor.props(anton, sebastian),
		// anton.getName()+sebastian.getName()+"ChatRoomActor" );
	}
	
	public static WebSocket<String> sockHandler() {
    	String username = session("username");

	    return WebSocket.withActor(new Function<ActorRef, Props>() {
	        public Props apply(ActorRef out) throws Throwable {
	        	// user aus session holen und "out" als ActorRef an User speichern
	    		//User user = UserRepository.getInstance().findByUsername(username);
	    		//akka://localhost:8080/system/websockets/35#1923723979
//	    		String address = out.path().address().host().toString();
//	    		String port = out.path().address().port().toString();
//	    		String string = out.path().toSerializationFormat();
//	    		System.out.println(string);
	    		
//	    		UserRepository.getInstance().setActorRefAtUser(user, string);
	        	ActorReferenceHolder.getInstance().addReference(username, out);
	        	return ChatRoomActor.props(out);
	        }
	    });
	}

//	public static WebSocket<JsonNode> sockHandler() {
//		WebSocket<JsonNode> ws = new WebSocket<JsonNode>() {
//			
//			private User user;
//			// called when the websocket is established
//			public void onReady(WebSocket.In<JsonNode> in,
//					WebSocket.Out<JsonNode> out) {
//				// register a callback for processing instream events
//				in.onMessage(new Callback<JsonNode>() {
//					public void invoke(JsonNode json) {
//
//						out.write(json);
//						Logger.info(json.toString());
//					}
//				});
//				
//				// write out a greeting
//				// out.write("I'm contacting you regarding your recent websocket.");
//			}
//		};
//		return ws;
//	}

	public static Result getAvailableUsers() {
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode node = nodeFactory.arrayNode();

		List<User> users = UserRepository.getInstance().findAll();

		for (User u : users) {
			ObjectNode result = Json.newObject();
			result.put("id", u.getId());
			result.put("text", u.getLastname() + ", " + u.getFirstname());
			result.put("disabled", false);
			node.add(result);
		}

		return ok(node);
	}

	@Transactional
	public static Result index() {
		return ok(views.html.index.render("Your new application is ready."));
	}

	@Security.Authenticated(Secured.class)
	public static Result chat() {
		return ok(views.html.chat.render(UserRepository.getInstance()
				.findByUsername(session().get("username"))));
	}

	public static Result registForm() {
		return ok(views.html.registForm.render(userForm));
	}

	public static Result loginForm() {
		return ok(views.html.login.render(userForm));
	}

	@Transactional
	public static Result submit() {
		Form<User> filledForm = userForm.bindFromRequest();
		User created = new User();
		DynamicForm requestData = Form.form().bindFromRequest();
		created.setFirstname(requestData.get("firstname"));
		created.setLastname(requestData.get("lastname"));
		created.setUsername(requestData.get("username"));
		created.setPassword(requestData.get("password"));
		UserRepository.getInstance().persist(created);
		return ok(views.html.submit.render(created));
	}

	/**
	 * Wird vom Registrierungsformular aufgerufen.
	 * 
	 * @return
	 */
	public static Result regist(String username) {
		// User anlegen

		return ok(views.html.registSuccess.render(username));
	}

	// @Security.Authenticated(Secured.class)
	// @Transactional
	// public static Result addUser(long userid) {
	// String username = Http.Context.current().session().get("username");
	// UserRepository rep = UserRepository.getInstance();
	// User sessionUser = rep.findByUsername(username);
	// User contact = rep.findById(userid);
	// rep.addUserToContactList(sessionUser, contact);
	// System.out.println(username + userid);
	// return noContent();
	// }

	@Transactional
	public static Result addContact() {
		DynamicForm requestData = Form.form().bindFromRequest();
		Long userid = Long.valueOf(requestData.get("contactid"));

		String username = Http.Context.current().session().get("username");

		UserRepository.getInstance().addUserToContactList(username, userid);

		return noContent();
	}

	@Transactional
	public static Result login() {

		Form<User> filledForm = userForm.bindFromRequest();
		DynamicForm requestData = Form.form().bindFromRequest();
		try {
			System.out.println("Nutzername " + requestData.get("username"));

			User user = UserRepository.getInstance().findByUsername(
					requestData.get("username"));
			// JPA.em().find(User.class, primaryKey)
			System.out.println("User Daten: " + user);
			String enteredPassword = requestData.get("password");
			if (user.getPassword().equals(enteredPassword)) {
				session("username", user.getUsername());
				return ok(views.html.chat.render(user));
			} else {
				return ok(views.html.registForm.render(userForm));
			}

		} catch (Exception e) {

			e.printStackTrace();
			return ok(views.html.errorPage.render(e.getMessage()));
		}
	}

	/**
	 * Controller action that constructs a MyMessage and sends it to our local
	 * Hello, World actor
	 *
	 * @param username
	 *            The name of the person to greet
	 * @return The promise of a Result
	 */
	@Transactional
	public static Promise<Result> localHello(String messageString) {

		Message message = new Message(messageString);
		message.setSource(new User("Anton"));
		message.setDestination(new User("Sebastian"));
		// MessageRepository.getInstance().persist(message);
		// Look up the actor
		ActorSelection myActor = actorSystem.actorSelection("user/"
				+ message.getSource().getUsername()
				+ message.getDestination().getUsername() + "ChatRoomActor");

		// As the actor for a response to the message (and a 30 second timeout);
		// ask returns an Akka Future, so we wrap it with a Play Promise
		return Promise.wrap(ask(myActor, message, 30000)).map(
				new Function<Object, Result>() {
					public Result apply(Object response) {
						if (response instanceof Message) {
							Message message = (Message) response;
							return ok(message.getMessage());
						}
						return notFound("Message is not of type Message");
					}
				});
	}

	@Transactional
	public static Result main() {
		return ok(views.html.main.render("Some Title",
				Html.apply("<span> bla </span>")));
	}

	@Transactional
	public static Result trivial(String name) {
		return ok(views.html.main
				.render("Title",
						Html.apply(""
								+ MessageRepository.getInstance().findAll()
								+ "")));
	}

	public static Result test(String name) {
		return ok("Hello " + name);

	}
}
