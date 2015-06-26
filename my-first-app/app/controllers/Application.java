package controllers;

import static akka.pattern.Patterns.ask;

import java.util.List;

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
import actors.ActorReferenceHolder;
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
	
	public static WebSocket<String> sockHandler() {
    	String username = session("username");

	    return WebSocket.withActor(new Function<ActorRef, Props>() {
	        public Props apply(ActorRef out) throws Throwable {
	        	ActorReferenceHolder.getInstance().addReference(username, out);
	        	return ChatRoomActor.props(out);
	        }
	    });
	}

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
	
	@Security.Authenticated(Secured.class)
	 public static Result logout() {
	    session().clear();
	    return ok(views.html.index.render("Your new application is ready."));
	  }

	
	@Transactional
	public static Result loadMessages() {
		
    	String username = session("username");
    	System.out.println(username);
		
		JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ArrayNode node = nodeFactory.arrayNode();

		List<Message> messages = MessageRepository.getInstance().findAllMessages(username);
		
		for (Message m : messages) {
			ObjectNode result = Json.newObject();
			result.put("message", m.getMessage());
			result.put("source", m.getSource().getUsername());
			result.put("destination", m.getDestination().getUsername());
			String timestamp = "";
			if (m.getTimestamp() != null)
				timestamp = m.getTimestamp().toString();
			result.put("timestamp", timestamp);
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
	public static Result signup() {
		Form<User> filledForm = userForm.bindFromRequest();
		User created = new User();
		DynamicForm requestData = Form.form().bindFromRequest();
		created.setFirstname(requestData.get("firstname"));
		created.setLastname(requestData.get("lastname"));
		created.setUsername(requestData.get("username"));
		created.setPassword(requestData.get("password"));
		UserRepository.getInstance().persist(created);
		return ok(views.html.signup.render(created));
	}

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
				return redirect("/chat");
			} else {
				return ok(views.html.registForm.render(userForm));
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ok(views.html.errorPage.render(e.getMessage()));
		}
	}

}
