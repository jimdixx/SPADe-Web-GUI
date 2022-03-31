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

    public String getDescriptionFromCatalogue() {
        String descriptionHeader = "## Summary ";
        String url = Constants.ANTI_PATTERN_CATALOGUE_URL_RAW + this.getCatalogueFileName();
        String html;

        try {
            html = Jsoup.connect(url).get().html();
        }
        catch (Exception e){
            /* If html from catalogue is not extracted, the description from anti-pattern configuration is returned */
            return this.getDescription();
        }

        String body = Jsoup.parse(html).body().text();

        /* Description parsing */
        int startIndex = body.indexOf(descriptionHeader);
        String resultDescription = "";

        if(startIndex == 0)
            return this.getDescription();

        int tmpIndex = startIndex + descriptionHeader.length(); // starting index position

        do {
            resultDescription += body.charAt(tmpIndex);
            tmpIndex ++;

            /* If the next headline is reached, the loop is exited */
            if(body.substring(tmpIndex, tmpIndex + 2).equals("##"))
                break;
        } while(tmpIndex < body.length() - 1);

        return resultDescription.trim();
    }

    public String getOperationalizationText() {
        String myPath = new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\webapp\\operationalizations\\" + this.getName() + ".html";
        String content = null;
        try {
            content = new String ( Files.readAllBytes(Paths.get(myPath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
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
