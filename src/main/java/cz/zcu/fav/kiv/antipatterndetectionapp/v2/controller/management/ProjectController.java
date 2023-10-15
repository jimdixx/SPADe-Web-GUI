package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types.Node;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


/**
 * This class contains all endpoints of projects.html
 */
@Controller
@RequestMapping("v2/management")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<String> getProjectsData() {
        ProjectToDto projectToDto = new ProjectToDto();
        List<Node> parents = new ArrayList<>();
        List<ProjectDto> parentProjects = projectToDto.convert(projectService.getParentProjects());
        for(ProjectDto parentProject : parentProjects) {
            Node n = new Node();
            n.project = parentProject;
            n.children = new ArrayList<>();
            parents.add(n);
        }

        for(Node n : parents) {
            n.children = projectService.calculate(n.project);
        }

        return ResponseEntity.status(parents.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(new Gson().toJson(parents));
    }

}

