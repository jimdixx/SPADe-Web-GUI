package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.*;

@Entity
@Table(name="configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //configuration json file
    private String config;
    //hash of the configuration so users cant upload duplicates
    private String configHash;
    // 'Y' if the configuration is accessible to everyone, 'N' if its user defined
    private char isDefault;

    public Configuration(){}
    public Configuration(String config, String configHash, boolean isDefault){
        this.config = config;
        this.configHash = configHash;
        this.isDefault = isDefault?'Y':'N';
    }


    public int getId() {
        return id;
    }

    public String getConfig() {
        return config;
    }

    public String getConfigHash() {
        return configHash;
    }

    public char getIsDefault() {
        return isDefault;
    }
}
