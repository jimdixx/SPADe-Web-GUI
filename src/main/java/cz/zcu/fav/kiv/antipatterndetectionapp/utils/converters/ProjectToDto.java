package cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.dto.ProjectDto;

import java.util.ArrayList;
import java.util.List;
public class ProjectToDto implements ClassToDto<Project, ProjectDto> {

    @Override
    public ProjectDto convert(Project source) {
        if (source == null) {
            return null;
        }
        return ProjectDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }

    @Override
    public List<ProjectDto> convert(List<Project> source) {
        List<ProjectDto> convertedList = new ArrayList<>();

        if(source == null){
            return convertedList;
        }
        source.stream().forEach(sourceItem->convertedList.add(this.convert(sourceItem)));
        return convertedList;
    }


}
