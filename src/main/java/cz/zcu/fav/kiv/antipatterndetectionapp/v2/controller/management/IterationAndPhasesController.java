package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PersonService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationAndPhasesToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PersonToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PhaseToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * This class contains all endpoints of management/persons
 */
@Controller
@RequestMapping("v2/management")
public class IterationAndPhasesController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/segment-iteration-phase")
    public ResponseEntity<String> getIterationAndPhasesFromProject(@RequestParam Map<String, String> requestData) {
        long ProjectId = Long.parseLong(requestData.get("projectId").toString());
        ObjectMapper objectMapper = new ObjectMapper();

        Project project = projectService.getProjectById(ProjectId);

        if (project == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Project not found");
        }

        List<Iteration> iterations = project.getIterations();
        List<Phase> phases = project.getPhases();

        List<IterationDto> iterationDtos = new IterationToDto().convert(iterations);
        List<PhaseDto> phasesDtos = new PhaseToDto().convert(phases);


        Collections.sort(iterationDtos, Comparator.comparing(IterationDto::getName, String.CASE_INSENSITIVE_ORDER));
        Collections.sort(phasesDtos, Comparator.comparing(PhaseDto::getName, String.CASE_INSENSITIVE_ORDER));

        IterationAndPhasesDto iterationAndPhasesDto = new IterationAndPhasesToDto().convert(iterationDtos, phasesDtos);

        try {
            String json = objectMapper.writeValueAsString(iterationAndPhasesDto);
            return ResponseEntity.status((iterations != null && phases != null) ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to serialize data to JSON");
        }
    }

}
