package controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import models.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class Users extends Action.Simple {

	@Transactional
    public Promise<Result> call(Http.Context ctx) throws Throwable {
//		EntityManager em = (EntityManager) ctx.args.get();
//		JPA.em(ctx.toString());
		TypedQuery<User> query = (TypedQuery<User>) JPA.em().createQuery("SELECT u FROM User u", User.class);
		
        ctx.args.put("users", query.getResultList());
        return delegate.call(ctx);
    }

	@Transactional
    public static List<User> current() {
        return (List<User>)Http.Context.current().args.get("users");
    }
}
