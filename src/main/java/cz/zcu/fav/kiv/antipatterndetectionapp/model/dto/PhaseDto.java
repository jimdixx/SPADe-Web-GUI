package cz.zcu.fav.kiv.antipatterndetectionapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDto {

    private long id;
    private String externalId;
    private String name;
    private String description;

    public static PhaseDtoBuilder builder() {return new PhaseDtoBuilder();}
}