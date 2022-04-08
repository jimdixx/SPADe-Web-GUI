package cz.zcu.fav.kiv.antipatterndetectionapp.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import org.jsoup.Jsoup;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Model class for AP.
 */
public class AntiPattern {

    private Long id;
    private String printName;
    private String name;
    private String description;
    private Map<String, Threshold> thresholds;
    private String catalogueFileName;

    public AntiPattern(Long id, String printName, String name, String description) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
    }

    public AntiPattern(Long id, String printName, String name, String description, Map<String, Threshold> thresholds) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
        this.thresholds = thresholds;
    }

    public AntiPattern(Long id, String printName, String name, String description, Map<String, Threshold> thresholds, String catalogueFileName) {
        this.id = id;
        this.printName = printName;
        this.name = name;
        this.description = description;
        this.thresholds = thresholds;
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

    public Map<String, Threshold> getThresholds() {
        return thresholds;
    }

    public void setThresholds(Map<String, Threshold> thresholds) {
        this.thresholds = thresholds;
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
                ", thresholds=" + thresholds +
                ", catalogueFileName='" + catalogueFileName + '\'' +
                '}';
    }
}
