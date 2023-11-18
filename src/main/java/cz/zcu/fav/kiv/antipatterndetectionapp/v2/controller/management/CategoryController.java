package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.ICategories;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.CategoryToDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * This class contains all endpoints connected to Categories
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("v2/management")
public class CategoryController {

    private final ICategories categories;

    @GetMapping("/getCategories")
    public ResponseEntity<GeneralResponseDto<CategoryDto>> getCategories(@RequestBody Map<String, String> requestData) {
        return categories.getResponse(requestData.get("projectId"));
    }

    @PostMapping("/changeCategory")
    public ResponseEntity<String> changeCategory() {
        //TODO
        return null;
    }
}
