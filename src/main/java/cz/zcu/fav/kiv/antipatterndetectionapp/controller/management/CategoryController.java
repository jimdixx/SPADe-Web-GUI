package cz.zcu.fav.kiv.antipatterndetectionapp.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.CategoryChangeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.ICategories;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * This class contains all endpoints connected to Categories
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("v2/management")
public class CategoryController {

    private final ICategories categories;

    @PostMapping("/getCategories")
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> getCategories(@RequestBody Map<String, String> requestData) {
        return categories.findCategoriesForProject(requestData.get("projectId"));
    }

    @PostMapping("/changeCategory")
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> changeCategory(@RequestBody CategoryChangeRequest categoryChangeRequest) {
        return categories.handleCategoryChangeRequest(categoryChangeRequest);
    }
}
