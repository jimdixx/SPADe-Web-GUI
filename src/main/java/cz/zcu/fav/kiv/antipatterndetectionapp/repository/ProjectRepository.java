package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
