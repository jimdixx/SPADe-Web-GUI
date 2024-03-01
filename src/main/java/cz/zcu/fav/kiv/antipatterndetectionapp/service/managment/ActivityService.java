package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.interfaces.DatabaseObject;

import java.util.List;

public interface ActivityService {
    List<Activity> fetchAllProjectActivities(long projectId);

    DatabaseObject saveActivity(Activity activity);

}
