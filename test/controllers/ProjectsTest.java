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

    @Test
    public void renameProject() {
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            long id = Project.find.where()
                    .eq("members.email", "bob@gmail.com")
                    .eq("name", "Private")
                    .findUnique().id;

            Result result = callAction(
                    controllers.routes.ref.Projects.rename(id),
                    fakeRequest()
                            .withSession("email", "bob@gmail.com")
                            .withFormUrlEncodedBody(ImmutableMap.of("name", "New Name"))
            );
            assertEquals(200, status(result));
            assertEquals("New Name", Project.find.byId(id).name);
        });
    }

    @Test
    public void renameProjectForbidden() {
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            long id = Project.find.where()
                    .eq("members.email", "bob@gmail.com")
                    .eq("name", "Private")
                    .findUnique().id;

            Result result = callAction(
                    controllers.routes.ref.Projects.rename(id),
                    fakeRequest()
                            .withSession("email", "notbob@nowhere.com")
                            .withFormUrlEncodedBody(ImmutableMap.of("name", "New Name"))
            );
            assertEquals(403, status(result));
            assertEquals("Private", Project.find.byId(id).name);
        });
    }

    @Test
    public void deleteProject() {
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            long id = Project.find.where()
                    .eq("members.email", "bob@gmail.com")
                    .eq("name", "Private")
                    .findUnique().id;

            Result result = callAction(
                    controllers.routes.ref.Projects.delete(id),
                    fakeRequest()
                            .withSession("email", "bob@gmail.com")
            );
            assertEquals(200, status(result));
            assertNull(Project.find.byId(id));
        });
    }

    @Test
    public void deleteProjectForbidden() {
        running(fakeApplication(inMemoryDatabase(), fakeGlobal()), () -> {
            Ebean.save((List) Yaml.load("test-data.yml"));

            long id = Project.find.where()
                    .eq("members.email", "bob@gmail.com")
                    .eq("name", "Private")
                    .findUnique().id;

            Result result = callAction(
                    controllers.routes.ref.Projects.delete(id),
                    fakeRequest()
                            .withSession("email", "notbob@nowhere.com")
            );
            assertEquals(403, status(result));
            assertNotNull(Project.find.byId(id));
        });
    }

    //Todo: Test Groups when you know what the hell they're doing.

}
