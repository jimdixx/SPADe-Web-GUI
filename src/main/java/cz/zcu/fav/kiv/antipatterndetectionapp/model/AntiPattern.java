package cz.zcu.fav.kiv.antipatterndetectionapp.model;

public class AntiPattern {

    private Long id;
    private String printName;
    private String name;
    private String description;

    public AntiPattern(Long id, String printName, String name, String description) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        return "AntiPattern{" +
                "id=" + id +
                ", printName='" + printName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
