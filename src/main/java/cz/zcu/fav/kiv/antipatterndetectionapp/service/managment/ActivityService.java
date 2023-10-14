package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;

import java.util.List;

public interface ActivityService {

    /**
     * Method getting all activities from database
     * @return  List of all activities
     */
    List<Activity> getAllActivities();

    /**
     * Method saving activity into database
     * @param activity  Activity tha will be safe
     * @return          Activity saved in database
     */
    Activity saveActivity(Activity activity);

    /**
     * Method getting activity by id
     * @param id    ID of activity
     * @return      Activity with that ID
     */
    Activity getActivityById(Long id);
}
