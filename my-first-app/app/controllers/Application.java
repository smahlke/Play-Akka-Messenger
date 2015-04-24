package controllers;

import play.*;
import play.mvc.*;
import play.twirl.api.Html;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is reayd."));
    }
    
    public static Result main() {
        return ok(main.render("Some Title", Html.apply("<span> asdf </span>")));
    }
    
    public static Result trivial(String name) {
        return ok("Hello " + name);
    }
    
    public static Result test(String name) {
        return ok("Hello " + name);
    }
}
