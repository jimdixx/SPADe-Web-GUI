package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.activity;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;

import java.util.List;

public interface ActivityService {
List<Activity> fetchAllProjectActivities(long projectId);

}
