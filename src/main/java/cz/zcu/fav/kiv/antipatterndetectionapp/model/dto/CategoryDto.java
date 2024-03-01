package cz.zcu.fav.kiv.antipatterndetectionapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    private String name;

    private String description;

    public static CategoryDtoBuilder builder() { return new CategoryDtoBuilder(); }

    public Long getId() {
        return id;
    }
}
