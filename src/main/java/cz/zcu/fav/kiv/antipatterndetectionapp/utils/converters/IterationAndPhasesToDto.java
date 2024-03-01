package cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.IterationAndPhasesDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.PhaseDto;

import java.util.List;

public class IterationAndPhasesToDto {

    public IterationAndPhasesDto convert (List<IterationDto> iterationDto, List<PhaseDto> phaseDtos) {
        if (iterationDto == null && phaseDtos == null) {
            return null;
        }

        return IterationAndPhasesDto.builder()
                .iterationDtos(iterationDto)
                .phaseDtos(phaseDtos)
                .build();
    }
}
