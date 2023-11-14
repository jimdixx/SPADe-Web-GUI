package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.managment;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationAndPhasesDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PhaseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationAndPhasesToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.IterationToDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.PhaseToDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class IterationsAndPhasesServiceImpl implements IterationsAndPhasesService {

    @Override
    public ResponseEntity<String> getIterationAndPhases(Project project) {
        ObjectMapper objectMapper = new ObjectMapper();

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

    @Override
    public ResponseEntity<String> changeToPhase(String[] iterationsIds) {
        return null;
    }

    @Override
    public ResponseEntity<String> changeToIteration(String[] phasesIds) {
        return null;
    }

}
