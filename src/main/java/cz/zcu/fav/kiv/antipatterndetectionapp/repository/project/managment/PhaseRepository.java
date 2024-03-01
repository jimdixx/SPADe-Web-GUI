package cz.zcu.fav.kiv.antipatterndetectionapp.repository.project.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.project.management.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Simple class that is implements JPA for easy loading project from DB.
 */
public interface PhaseRepository extends JpaRepository<Phase, Long> {

    @Query("select ph from Phase ph where ph.id IN :ids")
    List<Phase> fetchPhasesByIds(List<Long> ids);

}
