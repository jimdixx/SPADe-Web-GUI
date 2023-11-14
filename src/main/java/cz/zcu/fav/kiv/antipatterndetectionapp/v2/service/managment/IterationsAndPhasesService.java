package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import org.springframework.http.ResponseEntity;

public interface IterationsAndPhasesService {

    public ResponseEntity<String> getIterationAndPhases(Project project);

    public ResponseEntity<String> changeToPhase(String[] iterationsIds);

    public ResponseEntity<String> changeToIteration(String[] phasesIds);
}
