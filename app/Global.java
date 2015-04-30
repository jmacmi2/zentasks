import com.avaje.ebean.Ebean;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;

/**
 * Created by James on 30/04/2015.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(Application application) {
        super.onStart(application);

        //Note: isTest() here will bootstrap test classes for activator test, causing PK violations where test-data is manually strapped to tests.
        if(application.isDev() && User.find.findRowCount() == 0) {
            Ebean.save((List) Yaml.load("test-data.yml"));
        }
    }
}
