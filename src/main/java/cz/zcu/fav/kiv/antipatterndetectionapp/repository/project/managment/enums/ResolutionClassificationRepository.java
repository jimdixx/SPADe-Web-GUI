package cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.enums.ResolutionClassification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface ResolutionClassificationRepository extends JpaRepository<ResolutionClassification, Long> {
}
