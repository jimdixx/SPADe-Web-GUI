package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;


import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.PersonService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonMergeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PersonToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This class contains all endpoints of management/persons
 */
@Controller
@RequestMapping("v2/management")
public class IterationAndPhasesController {

    @Autowired
    private ProjectService projectService;

}
