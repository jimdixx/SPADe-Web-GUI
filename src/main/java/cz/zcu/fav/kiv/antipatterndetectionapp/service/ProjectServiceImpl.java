package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types.Node;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.ProjectRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        Collections.sort(projects, Comparator.comparing(Project::getName, String.CASE_INSENSITIVE_ORDER));
        return projects;
    }

    @Override
    public Project getProjectById(Long id) {
        Optional<Project> result = projectRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public List<Project> getAllProjectsForGivenIds(Long[] ids) {
        List<Project> projects = new ArrayList<>();

        for (Long id : ids) {
            projects.add(getProjectById(id));
        }
        return projects;
    }

    @Override
    public Project saveProject(Project project) {
        Project newProject = projectRepository.save(project);
        projectRepository.flush();
        return newProject;
    }

    @Override
    public List<Project> getParentProjects() {
        return projectRepository.getParentProjects();
    }

    @Override
    public List<Project> getSubordinateProjectsTo(Long id) {
        return projectRepository.getSubordinateProjectsTo(id);
    }

    @Override
    public ArrayList<Node> calculate(ProjectDto p) {
        ProjectToDto projectToDto = new ProjectToDto();
        ArrayList<Node> nodes = new ArrayList<>();
        List<ProjectDto> projects = projectToDto.convert(getSubordinateProjectsTo(p.getId()));
        for(ProjectDto project : projects) {
            Node n = new Node();
            n.project = project;
            n.children = calculate(project);
            nodes.add(n);
        }
        return nodes;
    }
}
