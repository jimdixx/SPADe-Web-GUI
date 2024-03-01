package cz.zcu.fav.kiv.antipatterndetectionapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IterationAndPhasesDto {
    
    private List<IterationDto> iterationDtos;
    private List<PhaseDto> phaseDtos;
    
    public static IterationAndPhasesDtoBuilder builder() {return new IterationAndPhasesDtoBuilder();}
}
