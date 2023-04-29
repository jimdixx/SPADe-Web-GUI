package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;


/**
 * @author Jiri Trefil
 * Wrapper for http request for uploading a new configuration by user
 */
public class UserConfiguration {
    private User user;
    private Configuration configuration;


    public UserConfiguration(User user, Configuration configuration){
        this.user = user;
        this.configuration = configuration;
    }
    public UserConfiguration(){}


    public User getUser() {
        return user;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
