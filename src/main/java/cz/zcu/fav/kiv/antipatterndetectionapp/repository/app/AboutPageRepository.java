package cz.zcu.fav.kiv.antipatterndetectionapp.repository.app;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.app.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutPageRepository extends JpaRepository<Metadata, Long> {

}
