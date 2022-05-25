package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.ProjectRepository;
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
        Collections.sort(projects, Comparator.comparing(Project::getName));
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
}
