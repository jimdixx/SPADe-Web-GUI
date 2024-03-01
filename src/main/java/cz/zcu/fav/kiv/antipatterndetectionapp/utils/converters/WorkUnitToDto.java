package cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.enums.WuType;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.WorkUnitDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkUnitToDto implements ClassToDto<WorkUnit, WorkUnitDto> {
    @Override
    public WorkUnitDto convert(WorkUnit source) {
        if (source == null) {
            return null;
        }
        Set<String> categories = new HashSet<>();
        for(Category category : source.getCategories()){
            categories.add(category.getName());
        }
        Activity activity = source.getActivity();
        String name = activity != null ? activity.getName() : "";

        Person ass = source.getAssignee();
        String assName = ass != null ? ass.getName() : "";

        WuType type = source.getType();
        String wuType = type != null ? type.getName() : "";
        return WorkUnitDto.builder()
                .startDate(source.getStartDate())
                .id(source.getId())
                .endDate(source.getDueDate())
                .type(wuType)
                .assignee(assName)
                .category(categories)
                .activity(name)
                .build();
    }

    @Override
    public List<WorkUnitDto> convert(List<WorkUnit> source) {
        List<WorkUnitDto> convertedList = new ArrayList<>();

        if(source == null){
            return convertedList;
        }
        source.stream().forEach(sourceItem->convertedList.add(this.convert(sourceItem)));
        return convertedList;
    }
}
