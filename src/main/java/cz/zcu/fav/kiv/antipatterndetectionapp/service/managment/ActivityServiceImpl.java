package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<Activity> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        Collections.sort(activities, Comparator.comparing(Activity::getName, String.CASE_INSENSITIVE_ORDER));
        return activities;
    }

    @Override
    public Activity saveActivity(Activity activity) {
        Activity newActivity = activityRepository.save(activity);
        activityRepository.flush();
        return newActivity;
    }

    @Override
    public Activity getActivityById(Long id) {
        Optional<Activity> result = activityRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }
}
