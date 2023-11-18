package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ICategories {

    ResponseEntity<GeneralResponseDto<CategoryDto>> getResponse(String projectId);

}
