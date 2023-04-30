package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import javax.persistence.*;

@Entity
@Table(name="configurations")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //configuration json file
    private String config;
    //hash of the configuration so users cant upload duplicates
    private String configHash;
    // 'Y' if the configuration is accessible to everyone, 'N' if its user defined
    private String isDefault;

    public Configuration(){}
    public Configuration(String config, String configHash, String isDefault){
        this.config = config;
        this.configHash = configHash;
        this.isDefault = isDefault;
    }

    public Configuration(int id){
        this.id = id;
    }

    public Configuration(String config, String isDefault){
        this.config = config;
        this.configHash = null;
        this.isDefault = isDefault;
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

    public String getIsDefault() {
        return isDefault;
    }


    public void setHash(String hash){
        this.configHash = hash;
    }

    @Override
    public String toString(){
        return this.config;
    }


}
