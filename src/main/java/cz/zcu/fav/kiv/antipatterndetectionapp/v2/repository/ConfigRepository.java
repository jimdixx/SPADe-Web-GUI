package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigKey;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigurationJoin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Query("select config, userconfig from Configuration config inner join UserConfigurationJoin userconfig on config.id = userconfig.id.configId where userconfig.id.userId = ?1 or config.isDefault = 'Y'")
    List<Object[]> getAllUserConfigurations(int userId);

    @Modifying
    @Transactional
    @Query("UPDATE Configuration cfg SET cfg.config = ?2, cfg.configHash= ?3 WHERE cfg.id = ?1")
    int updateHashAndConfigurationDefinition(int configurationId, String configurationDefinition, String configurationHash);



    @Query("select userconfig.configurationName,userconfig.id.configId from UserConfigurationJoin userconfig where userconfig.id.userId = ?1")
    List<Object[]> getAllUserConfigurationNames(int userId);

    @Query("select userconfig.configurationName from UserConfigurationJoin userconfig where userconfig.id = ?1")
    String findConfigurationByCompoundKey(UserConfigKey key);

    @Query("SELECT \n" +
            "cfg \n" +
            "FROM \n" +
            "UserConfigurationJoin userconfig INNER JOIN Configuration cfg \n" +
            "ON userconfig.id.configId = cfg.id\n" +
            "WHERE\n" +
            "userconfig.id = ?1")
    Configuration findConfigurationByUserNameAndID(UserConfigKey key);
}
