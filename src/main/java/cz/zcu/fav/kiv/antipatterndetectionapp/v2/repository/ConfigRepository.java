package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConfigRepository extends JpaRepository<Configuration,Integer> {
    Configuration findConfigurationById(int id);
    Configuration findConfigurationByConfigHash(String hash);
    //query to get all public configurations
    @Query("Select config from Configuration config where config.isDefault = 'Y'")
    List<Configuration> getPublicConfigurations();
    //retrieve all public configurations and configurations available to the user
    //assumption - default configurations are not assigned to any user explicitly
    @Query("select config from Configuration config where config.isDefault = 'Y' " +
            "or config.id in (select userconfig.id.userId from UserConfigurationJoin userconfig where userconfig.id.userId = ?1)")
    List<Configuration> getAllUserConfigurations(int userId);


}
