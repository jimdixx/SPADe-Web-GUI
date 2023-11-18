package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

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
@RequestMapping("v2/management")
public class CategoryController {

    @GetMapping("/getCategories")
    public ResponseEntity<String> getCategories() {
        //TODO
        return null;
    }

    @PostMapping("/changeCategory")
    public ResponseEntity<String> changeCategory() {
        //TODO
        return null;
    }
}
