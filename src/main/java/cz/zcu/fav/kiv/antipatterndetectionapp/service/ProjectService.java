package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;

import java.util.List;

public interface ProjectService {

    List<Project> getAllProjects();

    Project getProjectById(Long id);
}
