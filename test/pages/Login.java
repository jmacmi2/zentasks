package pages;

import org.fluentlenium.core.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

import components.*;
import controllers.*;

/**
 * Created by James on 04/05/2015.
 */
public class Login extends FluentPage {
    public String getUrl(){
        return controllers.routes.Application.login().url();
    }

    public void isAt(){
        assertThat(find("h1", withText("Sign In"))).hasSize(1);
    }

    public void login(String email, String password){
        fill("input").with(email, password);
        click("button");
    }
}
