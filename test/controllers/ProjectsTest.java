package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

/**
 * Created by James on 03/05/2015.
 */
public class ProjectsTest {
    @Test
    public void newProject(){
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            Result result = callAction(
                    controllers.routes.ref.Projects.add(),
                    fakeRequest().withSession("email", "bob@gmail.com")
                            .withFormUrlEncodedBody(ImmutableMap.of("group", "Some Group"))
            );
            assertEquals(200, status(result));
            Project project = Project.find.where()
                    .eq("folder", "Some Group").findUnique();
            assertNotNull(project);
            assertEquals("New project", project.name);
            assertEquals(1, project.members.size());
            assertEquals("bob@gmail.com", project.members.get(0).email);
        });

    }

}
