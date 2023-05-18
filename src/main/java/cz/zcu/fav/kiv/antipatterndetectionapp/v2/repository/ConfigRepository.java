package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigurationJoin;
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
    @Query("select config, userconfig from Configuration config left join UserConfigurationJoin userconfig on config.id = userconfig.id.configId where userconfig.id.userId = ?1 or config.isDefault = 'Y'")
    List<Object[]> getAllUserConfigurations(int userId);

    //todo default user id parametrem
    @Query("select userconfig.configurationName,userconfig.id.configId from UserConfigurationJoin userconfig where userconfig.id.userId = ?1")
    List<Object[]> getAllUserConfigurationNames(int userId);

    @Query("select userconfig.configurationName from UserConfigurationJoin userconfig where userconfig.id = ?1")
    String findConfigurationByCompoundKey(UserConfigKey key);
}
