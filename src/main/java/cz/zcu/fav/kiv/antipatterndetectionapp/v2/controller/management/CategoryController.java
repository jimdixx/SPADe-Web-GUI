package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.ICategories;
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
    public ResponseEntity<GeneralResponseDto<CategoryDto>> getCategories(@RequestBody Map<String, String> requestData) {
        return categories.getResponse(requestData.get("projectId"));
    }

    @PostMapping("/changeCategory")
    public ResponseEntity<String> changeCategory() {
        //TODO
        return null;
    }
}
