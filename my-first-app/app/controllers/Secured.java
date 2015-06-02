package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
import views.html.*;

public class Secured extends Security.Authenticator {

	@Override
    public String getUsername(Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return ok(views.html.login.render(Application.userForm));
    }

}
