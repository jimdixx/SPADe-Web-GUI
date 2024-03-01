package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.CategoryChangeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.GeneralResponseDto;
import org.springframework.http.ResponseEntity;

public interface ICategories {

    /**
     * Returns the list of categories for given project id
     * @param projectId project ID
     * @return ResponseEntity with the list of categories
     */
    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> findCategoriesForProject(String projectId);

    /**
     * Manage the request for categories change according to the given data
     * @param categoryChangeRequest Input data with categories to change and type of the operation
     * @return ResponseEntity with the results of the operation
     */
    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> handleCategoryChangeRequest(CategoryChangeRequest categoryChangeRequest);

}
