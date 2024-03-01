package cz.zcu.fav.kiv.antipatterndetectionapp.model.dto;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.IdentityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;
    private String name;

    private List<IdentityDto> identities;

    public static PersonDtoBuilder builder() {return new PersonDtoBuilder();}
}
