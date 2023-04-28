package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserConfigKey implements Serializable {

private int userId;
private int configId;

public UserConfigKey(int userId,int configId){
    this.userId = userId;
    this.configId = configId;
}


    public UserConfigKey() {

    }
}
