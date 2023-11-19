package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ICategories {

    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> findCategoriesForProject(String projectId);

    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> handleCategoryChangeRequest(@Nullable List<CategoryDto> selectedCategories,
                                                                                                                  @Nullable Integer submitType,
                                                                                                                  @Nullable String submitId);

}
