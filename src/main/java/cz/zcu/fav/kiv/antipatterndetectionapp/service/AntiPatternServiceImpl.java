package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.CacheablesValues;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Threshold;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


@Service
public class AntiPatternServiceImpl implements AntiPatternService {

    @Autowired
    private AntiPatternRepository antiPatternRepository;

    // class that stores cached values
    private CacheablesValues cacheablesValues = new CacheablesValues();

    @Override
    public List<AntiPatternDetector> getAllAntiPatterns() {
        return antiPatternRepository.getAllAntiPatterns();
    }

    @Override
    public AntiPatternDetector getAntiPatternById(Long id) {
        return antiPatternRepository.getAntiPatternById(id);
    }

    @Override
    public List<AntiPattern> antiPatternsToModel(List<AntiPatternDetector> antiPatternDetectors) {
        List<AntiPattern> antiPatterns = new LinkedList<>();
        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            AntiPattern antiPattern = antiPatternDetector.getAntiPatternModel();

            // set to default value
            for (Map.Entry<String, Threshold> threshold : antiPattern.getThresholds().entrySet()) {
                threshold.getValue().setErrorMessageShown(false);
            }
            antiPatterns.add(antiPattern);
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern antiPatternToModel(AntiPatternDetector antiPatternDetector) {
        AntiPattern antiPattern = antiPatternDetector.getAntiPatternModel();
        // set to default value
        for (Map.Entry<String, Threshold> threshold : antiPattern.getThresholds().entrySet()) {
            threshold.getValue().setErrorMessageShown(false);
        }
        return antiPattern;
    }

    @Override
    public List<AntiPatternDetector> getAllAntiPatternsForGivenIds(Long[] ids) {
        List<AntiPatternDetector> antiPatternDetectors = new ArrayList<>();

        for (Long id : ids) {
            antiPatternDetectors.add(antiPatternRepository.getAntiPatternById(id));
        }
        return antiPatternDetectors;
    }

    @Override
    public void saveAnalyzedProjects(String[] selectedProjects, String[] selectedAntiPatterns) {
        this.cacheablesValues.setAnalyzedProjects(selectedProjects);
        this.cacheablesValues.setAnalyzedAntiPatterns(selectedAntiPatterns);
    }

    @Override
    public String[] getAnalyzedProjects() {
        return this.cacheablesValues.getAnalyzedProjects();
    }

    @Override
    public String[] getAnalyzedAntiPatterns() {
        return this.cacheablesValues.getAnalyzedAntiPatterns();
    }

    @Override
    public boolean isConfigurationChanged() {
        return this.cacheablesValues.isConfigurationChanged();
    }

    @Override
    public void setConfigurationChanged(boolean configurationChanged) {
        this.cacheablesValues.setConfigurationChanged(configurationChanged);
    }

    @Override
    public void saveResults(List<QueryResult> results) {
        this.cacheablesValues.setResults(results);
    }

    @Override
    public List<QueryResult> getResults() {
        return this.cacheablesValues.getResults();
    }

    @Override
    public List<AntiPattern> setErrorMessages(List<AntiPattern> antiPatterns, List<String> wrongParameters) {
        for (AntiPattern antiPattern : antiPatterns) {
            for (Map.Entry<String, Threshold> threshold : antiPattern.getThresholds().entrySet()) {
                if (wrongParameters.contains(threshold.getKey())) {
                    threshold.getValue().setErrorMessageShown(true);
                } else {
                    threshold.getValue().setErrorMessageShown(false);
                }
            }
        }
        return antiPatterns;
    }

    @Override
    public AntiPattern setErrorMessages(AntiPattern antiPattern, List<String> wrongParameters) {
        List<AntiPattern> antiPatterns = new ArrayList<>();
        antiPatterns.add(antiPattern);
        return setErrorMessages(antiPatterns, wrongParameters).get(0);
    }

    @Override
    public String getDescriptionFromCatalogue(long id) {
        AntiPattern ap = this.getAntiPatternById(id).getAntiPatternModel();

        String descriptionHeader = "## Summary ";
        String url = Constants.ANTI_PATTERN_CATALOGUE_URL_RAW + ap.getCatalogueFileName();
        String html;

        try {
            html = Jsoup.connect(url).get().html();
        }
        catch (Exception e){
            /* If html from catalogue is not extracted, the description from anti-pattern configuration is returned */
            return ap.getDescription();
        }

        String body = Jsoup.parse(html).body().text();

        /* Description parsing */
        int startIndex = body.indexOf(descriptionHeader);
        String resultDescription = "";

        if(startIndex == 0)
            return ap.getDescription();

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

    @Override
    public String getOperationalizationText(String fileName){
        String filePath = getOperationalizationFilePath(fileName);

        String content = null;
        try {
            content = new String (Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public String getOperationalizationFilePath(String antiPatternName){
        return antiPatternRepository.getOperationalizationDirPathName() + "/" + antiPatternName + ".html";
    }

    @Override
    public String getOperationalizationImageFilePath(String imageName){
        return antiPatternRepository.getOperationalizationImgDirFileName() + "/" + imageName;
    }
}
