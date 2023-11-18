package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Long id;

    private String name;

    private String description;

    public static CategoryDtoBuilder builder() { return new CategoryDtoBuilder(); }

}
