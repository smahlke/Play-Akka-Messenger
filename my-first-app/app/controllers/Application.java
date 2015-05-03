package controllers;

import models.Message;
import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.index;
import views.html.main;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is reayd."));
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
        return ok(main.render("Title", Html.apply(""+Message.findAllMessages() +"")));
    }
    
    public static Result test(String name) {
    	return ok("Hello " + name);
        
    }
}
