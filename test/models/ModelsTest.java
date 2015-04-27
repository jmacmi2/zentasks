package models;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 * Created by James on 27/04/2015.
 */
public class ModelsTest {
    @Test
    public void createAndRetrieveUser(){
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            new User("bob@gmail.com", "Bob", "Secret").save();

            User bob = User.find.where().eq("email", "bob@gmail.com").findUnique();
            assertNotNull(bob);
            assertEquals("Bob", bob.name);
        });
    }

    @Test
    public void tryAuthenticateUser(){
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            new User("bob@gmail.com", "Bob", "Secret").save();

            assertNotNull(User.authenticate("bob@gmail.com", "Secret"));
            assertNull(User.authenticate("bob@gmail.com", "BadPassword"));
            assertNull(User.authenticate("badEmail", "Secret"));
        });
    }

    @Test
    public void findProjectsInvolving(){
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            new User("bob@gmail.com", "Bob", "Secret").save();
            new User("jane@gmail.com", "Jane", "Secret").save();

            Project.create("Play 2", "play", "bob@gmail.com");
            Project.create("Play 1", "play", "jane@gmail.com");

            List<Project> bobsProjects = Project.findInvolving("bob@gmail.com");
            assertNotNull(bobsProjects);
            assertEquals(1, bobsProjects.size());
            assertEquals("Play 2", bobsProjects.get(0).name);

        });
    }

    @Test
    public void findTodoTasksInvolving(){
        running(fakeApplication(inMemoryDatabase("test")), () -> {
            User bob = new User("bob@gmail.com", "Bob", "Secret");
            bob.save();

            Project project = Project.create("Play 2", "play", "bob@gmail.com");

            Task t1 = new Task();
            t1.title = "Write tutorial";
            t1.assignedTo = bob;
            t1.done = true;
            t1.save();

            Task t2 = new Task();
            t2.title = "Release next version";
            t2.project = project;
            t2.save();

            List<Task> results = Task.findTodoInvolving("bob@gmail.com");
            assertNotNull(results);
            assertEquals(1, results.size());
            assertEquals("Release next version", results.get(0).title);
        });

    }

}
