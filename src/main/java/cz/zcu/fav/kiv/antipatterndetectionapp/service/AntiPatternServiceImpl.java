package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.CacheablesValues;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Threshold;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.AntiPatternRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

    public List<String> saveNewConfiguration(String[] thresholdNames, String[] thresholdValues) {
        List<AntiPatternDetector> antiPatternDetectors = antiPatternRepository.getAllAntiPatterns();
        List<String> incorrectParameters = new ArrayList<>();

        for (AntiPatternDetector antiPatternDetector : antiPatternDetectors) {
            // not every anti-pattern should have thresholds
            if (antiPatternDetector.getAntiPatternModel().getThresholds() == null) {
                continue;
            }
            for (int i = 0; i < thresholdNames.length; i++) {
                if (antiPatternDetector.getAntiPatternModel().getThresholds().containsKey(thresholdNames[i])) {

                    if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == Integer.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((Integer.parseInt(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }

                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == Percentage.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((Percentage.parsePercentage(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == PositiveInteger.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((PositiveInteger.parsePositiveInteger(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == PositiveFloat.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((PositiveFloat.parsePositiveFloat(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == Float.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((Float.parseFloat(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == Double.class) {
                        try {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((Double.parseDouble(thresholdValues[i])));
                            setConfigurationChanged(true);
                        } catch (NumberFormatException e) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    } else if (antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).getValue().getClass() == String.class) {
                        if (Utils.checkStringSubstrings(thresholdValues[i])) {
                            antiPatternDetector.getAntiPatternModel().getThresholds().get(thresholdNames[i]).setValue((thresholdValues[i]));
                            setConfigurationChanged(true);

                        } else {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                }
            }
        }
        return incorrectParameters;
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
    public AntiPattern getAntiPatternFromJsonFile(String jsonFileName){
        String json = "";   // json configuration file
        JsonNode node = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(new FileSystemResource("").getFile().getAbsolutePath() + "\\src\\main\\webapp\\antipatterns\\" + jsonFileName)));
            node = JsonParser.parse(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Long APid = Long.parseLong(node.get("id").asText());
        String APPrintName = node.get("printName").asText();
        String APName = node.get("name").asText();
        String APDescription = node.get("description").asText();
        String APCatalogueFileName = node.get("catalogueFileName") != null ? node.get("catalogueFileName").asText() : null;

        Map<String, Threshold> APMap = new HashMap<>();

        JsonNode array = node.get("thresholds");
        Threshold<?> tmpThreshold = null;

        for(int i = 0; i < array.size(); i++){
            JsonNode tmpNode = array.get(i);

            String thresholdName = tmpNode.get("thresholdName").asText();
            String thresholdType = tmpNode.get("thresholdType").asText();

            JsonNode thresholdNode = tmpNode.get("threshold");

            String tThresholdName = thresholdNode.get("thresholdName").asText();
            String tThresholdPrintName = thresholdNode.get("thresholdPrintName").asText();
            String tThresholdDescription = thresholdNode.get("thresholdDescription").asText();
            String tThresholdErrorMess = thresholdNode.get("thresholdErrorMess").asText();
            String tThresholdValue = thresholdNode.get("thresholdValue").asText();

            tmpThreshold = getThreshold(thresholdType, tThresholdName, tThresholdPrintName, tThresholdDescription, tThresholdErrorMess, tThresholdValue);

            APMap.put(thresholdName, tmpThreshold);
        }

        AntiPattern newAP = new AntiPattern(APid, APPrintName, APName, APDescription, APMap, APCatalogueFileName);

        return newAP;
    }

    private Threshold<?> getThreshold(String thresholdType, String name, String printName, String description, String errorMessage, String value){

        if(thresholdType.equals("Percentage")){
            return new Threshold<>(name, printName, description, errorMessage, new Percentage(Float.parseFloat(value)));
        }
        else if(thresholdType.equals("PositiveFloat")){
            return new Threshold<>(name, printName, description, errorMessage, new PositiveFloat(Float.parseFloat(value)));
        }
        else if(thresholdType.equals("PositiveInteger")){
            return new Threshold<>(name, printName, description, errorMessage, new PositiveInteger(Integer.parseInt(value)));
        }
        else if(thresholdType.equals("String")){
            return new Threshold<>(name, printName, description, errorMessage, value);
        }

        return null;
    }

}
