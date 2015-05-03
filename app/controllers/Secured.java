package controllers;

import models.Project;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by James on 30/04/2015.
 */
public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        return context.session().get("email");

    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect(routes.Application.login());
    }

    public static boolean isMemberOf(Long project){
        return Project.isMember(
                project,
                Http.Context.current().request().username()
                );

    }
}
