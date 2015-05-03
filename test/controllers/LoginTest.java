package controllers;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;
import org.junit.*;
import static org.junit.Assert.*;
import play.libs.*;
import play.mvc.*;
import play.test.*;


import java.util.*;

import static play.test.Helpers.*;

/**
 * Created by James on 30/04/2015.
 */
public class LoginTest {
    @Test
    public void authenticationSuccess(){
        running(fakeApplication(inMemoryDatabase("test"), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            Result result = callAction(
                    controllers.routes.ref.Application.authenticate(),
                    fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                            "email", "bob@gmail.com",
                            "password", "secret"
                    ))
            );

            assertEquals(303, status(result));
            assertEquals("bob@gmail.com", session(result).get("email"));
        });
    }

    @Test
    public void authenticationFailure(){
        running(fakeApplication(inMemoryDatabase("test"), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            Result result = callAction(
                    controllers.routes.ref.Application.authenticate(),
                    fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                            "email", "bob@gmail.com",
                            "password", "badpassword"
                    ))
            );

            assertEquals(400, status(result));
            assertNull(session(result).get("email"));
        });
    }

    @Test
    public void authenticated(){
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            Result result = callAction(
                    controllers.routes.ref.Application.index(),
                    fakeRequest().withSession("email", "bob@gmail.com")
            );
            assertEquals(200, status(result));
        });
    }

    @Test
    public void notAuthenticated(){
        running(fakeApplication(inMemoryDatabase(),fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            Result result = callAction(
                    controllers.routes.ref.Application.index(),
                    fakeRequest()
            );

            assertEquals(303, status(result));
            assertEquals("/login", header("Location", result));
        });
    }
}
