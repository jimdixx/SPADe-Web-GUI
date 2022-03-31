package cz.zcu.fav.kiv.antipatterndetectionapp.model;

/**
 * Model class for threshold.
 * @param <T> threshold can have different data types
 */
public class Threshold<T> {
    private String name;
    private String printName;
    private String description;
    private String errorMessage;
    private boolean isErrorMessageShown;
    private T value;

    public Threshold(String name, String printName, String description, T value) {
        this.name = name;
        this.printName = printName;
        this.description = description;
        this.value = value;
        this.isErrorMessageShown = false;
    }

    public Threshold(String name, String printName, String description, String errorMessage, T value) {
        this.name = name;
        this.printName = printName;
        this.description = description;
        this.errorMessage = errorMessage;
        this.value = value;
        this.isErrorMessageShown = false;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isErrorMessageShown() {
        return isErrorMessageShown;
    }

    public void setErrorMessageShown(boolean errorMessageShown) {
        isErrorMessageShown = errorMessageShown;
    }
}
