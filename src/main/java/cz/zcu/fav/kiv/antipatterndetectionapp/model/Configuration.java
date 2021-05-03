package cz.zcu.fav.kiv.antipatterndetectionapp.model;

public class Configuration<T> {
    private String name;
    private String printName;
    private String description;
    private T value;

    public Configuration(String name, String printName, String description, T value) {
        this.name = name;
        this.printName = printName;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
