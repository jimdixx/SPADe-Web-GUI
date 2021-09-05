package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;

import java.util.Map;

/**
 * Model class for AP.
 */
public class AntiPattern {

    private Long id;
    private String printName;
    private String name;
    private String description;
    private Map<String, Configuration> configurations;
    private String catalogueFileName;

    public AntiPattern(Long id, String printName, String name, String description) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
    }

    public AntiPattern(Long id, String printName, String name, String description, Map<String, Configuration> configurations) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
        this.configurations = configurations;
    }

    public AntiPattern(Long id, String printName, String name, String description, Map<String, Configuration> configurations, String catalogueFileName) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
        this.configurations = configurations;
        this.catalogueFileName = catalogueFileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Configuration> configurations) {
        this.configurations = configurations;
    }

    public String getUrl() {
        return "<a href='/anti-patterns/" + this.id.toString() + "'>Detail</a>";
    }

    public String getCatalogueFileName() {
        return catalogueFileName;
    }

    public void setCatalogueFileName(String catalogueFileName) {
        this.catalogueFileName = catalogueFileName;
    }

    public String getFullCatalogueUrl() {
        return Constants.ANTI_PATTERN_CATALOGUE_URL + this.catalogueFileName;
    }

    @Override
    public String toString() {
        return "AntiPattern{" +
                "id=" + id +
                ", printName='" + printName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", configurations=" + configurations +
                ", catalogueFileName='" + catalogueFileName + '\'' +
                '}';
    }
}
