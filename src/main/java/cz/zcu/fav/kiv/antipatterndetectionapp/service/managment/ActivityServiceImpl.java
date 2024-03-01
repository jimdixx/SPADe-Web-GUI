package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.controller.management.SegmentActivityController;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.interfaces.DatabaseObject;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment.ActivityRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums.WuTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final Logger LOGGER = LoggerFactory.getLogger(SegmentActivityController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkUnitService workUnitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WuTypeService wuTypeService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private WorkUnitRepository workUnitRepository;

    @Override
    public List<Activity> fetchAllProjectActivities(long projectId) {
        Project p = this.projectService.getProjectById(projectId);
        if(p == null){
            return null;
        }
        List<Activity> activities = p.getActivities();
        return activities;
    }

    @Override
    public DatabaseObject saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }
}
