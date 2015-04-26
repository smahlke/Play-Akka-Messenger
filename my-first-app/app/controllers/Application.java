package controllers;

import models.User;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.index;
import views.html.main;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is reayd."));
    }
    
    @play.db.jpa.Transactional
    public static Result main() {
    	
    	User u = new User();
    	u.setName("jiiiiipiii");
    	u.setId(new Long(0L));
    	JPA.em().persist(u);

        return ok(main.render("Some Title", Html.apply("<span> " + User.findById(0L).getName() + " </span>")));
    }
    
    public static Result trivial(String name) {
        return ok("Hello " + name);
    }
    
    public static Result test(String name) {
    	return ok("Hello " + name);
        
    }
}
