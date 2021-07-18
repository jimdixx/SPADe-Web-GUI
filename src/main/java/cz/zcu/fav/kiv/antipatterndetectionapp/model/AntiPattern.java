package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import java.util.Map;

public class AntiPattern {

    private Long id;
    private String printName;
    private String name;
    private String description;
    private Map<String, Configuration> configurations;

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

    @Override
    public String toString() {
        return "AntiPattern{" +
                "id=" + id +
                ", printName='" + printName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", configurations=" + configurations +
                '}';
    }
}
