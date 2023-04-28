package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Jiri Trefil
 * Join table between user and configuration
 * One user can have multiple (or none) configurations associated with him
 * One configuration can have associated multiple users
 * there this Many:Many relationship has to be decomposed with this table
 * Primary key is a Composite key (userId+configId is always a unique pair)
 */
@Entity
@Table(name="user_configurations")
public class UserConfiguration {
    @EmbeddedId
    private UserConfigKey id;


    public UserConfiguration(){
    }

    public UserConfiguration(UserConfigKey id){
        this.id = id;
    }
    public UserConfigKey getUserConfigKey(){
        return this.id;
    }
}
