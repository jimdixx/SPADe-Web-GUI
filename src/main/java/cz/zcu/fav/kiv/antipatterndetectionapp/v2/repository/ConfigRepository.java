package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ConfigRepository extends JpaRepository<Configuration,Integer> {
    @Value("${default_user_id}")
    int defaultUserId = 0;
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

    //todo default user id parametrem
    @Query("select userconfig.configurationName from UserConfigurationJoin userconfig where userconfig.id.userId = ?1 or userconfig.id.userId=2")
    List<String> getAllUserConfigurationNames(int userId);

    @Query("select userconfig.configurationName from UserConfigurationJoin userconfig where userconfig.id = ?1")
    String findConfigurationByCompoundKey(UserConfigKey key);




}
