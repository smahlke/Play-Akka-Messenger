package controllers;

import models.User;
import play.*;
import play.mvc.*;
import play.twirl.api.Html;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is reayd."));
    }
    
    public static Result main() {
    	User u = new User();
    	u.setName("jiiiiipiii");
    	u.setId(new Long(0L));
    	u.save();
        return ok(main.render("Some Title", Html.apply("<span> " + User.find.all().get(0) + " </span>")));
    }
    
    public static Result trivial(String name) {
    	User u = new User();
    	u.setName(name);
    	u.setId(new Long(0L));
    	u.save();
        return ok("Hello " + name);
    }
    
    public static Result test(String name) {
        User u = new User(name);
        u.save();
        
    	return ok("Hello " + name);
        
    }
}
