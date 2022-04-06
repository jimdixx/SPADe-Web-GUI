package cz.zcu.fav.kiv.antipatterndetectionapp.model;

/**
 * Model class for threshold.
 */
public class Threshold {
    private String name;
    private String printName;
    private String description;
    private String errorMessage;
    private String type;
    private boolean isErrorMessageShown;

    public Threshold(String name, String printName, String description) {
        this.name = name;
        this.printName = printName;
        this.description = description;
        this.isErrorMessageShown = false;
    }

    public Threshold(String name, String printName, String description, String errorMessage, String type) {
        this.name = name;
        this.printName = printName;
        this.description = description;
        this.errorMessage = errorMessage;
        this.type = type;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isErrorMessageShown() {
        return isErrorMessageShown;
    }

    public void setErrorMessageShown(boolean errorMessageShown) {
        isErrorMessageShown = errorMessageShown;
    }
}
