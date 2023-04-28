package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, UserConfigKey> {
}
