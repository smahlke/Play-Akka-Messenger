package controllers;

import java.util.Collection;
import java.util.List;

import models.User;
import models.repository.UserRepository;
import play.db.jpa.Transactional;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class Users extends Action.Simple {

	@Transactional
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        ctx.args.put("users", UserRepository.getInstance().findAll());
        return delegate.call(ctx);
    }

	@Transactional
    public static List<User> current() {
		return (List<User>)Http.Context.current().args.get("users");
    }
	
	@Transactional
    public static Collection<User> contactList() {
		String username = Http.Context.current().session().get("username");
		User user = UserRepository.getInstance().findByUsername(username);
		return user.getContactList();
    }

}
