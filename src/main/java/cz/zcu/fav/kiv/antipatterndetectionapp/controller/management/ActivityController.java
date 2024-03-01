package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.ActivityDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.ActivityService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters.ActivityToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters.ClassToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v2/management")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    //"/v2/management/activity_list"
    @GetMapping("/activity_list")
    public ResponseEntity<String> getProjectsList(@RequestParam Long projectId) {

        //ProjectToDto projectToDto = new ProjectToDto();
        //List<ProjectDto> projects = projectToDto.convert(projectService.getAllProjects());
        List<Activity> projectActivities = activityService.fetchAllProjectActivities(projectId);
        if(projectActivities == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ClassToDto<Activity, ActivityDto> mapper = new ActivityToDto();
        List<ActivityDto> activityDto = mapper.convert(projectActivities);

        return ResponseEntity.status(HttpStatus.OK).body(new Gson().toJson(activityDto  ));
    }

}
