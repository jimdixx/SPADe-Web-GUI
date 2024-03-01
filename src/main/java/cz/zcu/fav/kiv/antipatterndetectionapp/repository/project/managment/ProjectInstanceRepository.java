package cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.ProjectInstance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface ProjectInstanceRepository extends JpaRepository<ProjectInstance, Long> {
}
