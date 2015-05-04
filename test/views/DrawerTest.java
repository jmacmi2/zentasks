package views;

import org.junit.*;

import play.test.*;
import static play.test.Helpers.*;

import org.fluentlenium.core.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import com.google.common.base.*;

import pages.*;
import components.*;

public class DrawerTest extends WithBrowser {

    public Drawer drawer;
    public Dashboard dashboard;

    @Before
    public void setUp() {
        start();
        Login login = browser.createPage(Login.class);
        login.go();
        login.login("bob@gmail.com", "secret");
        dashboard = browser.createPage(Dashboard.class);
        drawer = dashboard.drawer();
    }

    @Test
    public void newProject() throws Exception {
        drawer.group("Personal").newProject();
        dashboard.await().until(drawer.group("Personal").hasProject("New project"));
        dashboard.await().until(drawer.group("Personal").project("New project").isInEdit());
    }

    @Test
    public void renameProject() throws Exception {
        drawer.group("Personal").project("Private").rename("Confidential");
        dashboard.await().until(Predicates.not(drawer.group("Personal").hasProject("Private")));
        dashboard.await().until(drawer.group("Personal").hasProject("Confidential"));
        dashboard.await().until(Predicates.not(drawer.group("Personal").project("Confidential").isInEdit()));
    }
}