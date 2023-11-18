package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.impl;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.ICategories;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.CategoryToDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoriesImpl implements ICategories {

    private final ProjectService projectService;
    private CategoryToDto categoryMapper = new CategoryToDto();
    private final String PROJECT_NOT_FOUND_MESSAGE = "Project not found!";
    private final String CATEGORIES_NOT_FOUND_MESSAGE = "Categories not found for the project!";
    private final String INVALID_PROJECT_ID = "Provided project ID is in invalid format!";

    private Project getProjectById(Long id) {
        return projectService.getProjectById(id);
    }

    private List<CategoryDto> getCategories(Project project) {

        if (project == null) {
            return null;
        }

        List<Category> categories = new ArrayList<>();
        for (ProjectInstance projectInstance : project.getProjectInstances()) {
            categories.addAll(projectInstance.getCategories());
        }

        List<CategoryDto> categoriesDto = categoryMapper.convert(categories);
        categoriesDto.sort(Comparator.comparing(CategoryDto::getName, String.CASE_INSENSITIVE_ORDER));

        return categoriesDto;
    }

    @Override
    public ResponseEntity<GeneralResponseDto<CategoryDto>> getResponse(String projectId) {
        // --- The value from the request body is String, so there must be a validation if the string is number or not.
        final Long id = tryParse(projectId);
        if (id == Long.MIN_VALUE)
            return createResponseEntity(HttpStatus.BAD_REQUEST, INVALID_PROJECT_ID, null);

        // --- get project by ID
        final Project project = getProjectById(id);

        if (project == null)
            return createResponseEntity(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND_MESSAGE, null);

        // --- Get all categories for the specific project
        final List<CategoryDto> categories = getCategories(project);

        if (categories == null)
            return createResponseEntity(HttpStatus.NOT_FOUND, CATEGORIES_NOT_FOUND_MESSAGE, null);

        return createResponseEntity(HttpStatus.OK, null, categories);
    }

    private ResponseEntity<GeneralResponseDto<CategoryDto>> createResponseEntity(@NonNull HttpStatus status,
                                                                                 @Nullable String message,
                                                                                 @Nullable List<CategoryDto> categories) {


        return new ResponseEntity<>(
                GeneralResponseDto
                        .<CategoryDto>builder()
                        .message(message)
                        .categories(categories)
                        .build(), status);
    }

    private Long tryParse(String valueToParse) {
        try {
            return Long.parseLong(valueToParse);
        } catch (NumberFormatException e) {
            return Long.MIN_VALUE;
        }
    }

}
