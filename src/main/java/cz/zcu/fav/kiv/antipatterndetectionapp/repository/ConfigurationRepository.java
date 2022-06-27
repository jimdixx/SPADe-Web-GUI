package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.Percentage;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveFloat;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.types.PositiveInteger;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class ConfigurationRepository {

    private final String CONFIGURATION_DIR;
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigurationRepository.class);

    // Map<ConfigurationName, Map<AntiPatternName, Map<ThresholdName, value>>>
    public Map<String, Map<String, Map<String, String>>> allConfigurations;

    /**
     * Constructor to set configurations folder and start loading
     * @param dir
     */
    public ConfigurationRepository(@Value("${DATA_PATH}")String dir){
        this.CONFIGURATION_DIR = dir + "configurations/";
        loadConfigurations();
    }

    /**
     * Method for loading all configurations from json files in folder and saving them to map structure
     */
    private void loadConfigurations(){
        LOGGER.info("-------START READING CONFIGURATIONS FROM FILES-------");

        Map<String, Map<String, Map<String, String>>> configurations = new HashMap<>();

        File configurationsFolder = null;
        try {
            configurationsFolder = new File(new FileSystemResource(CONFIGURATION_DIR).getFile().getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Cannot access folder with configurations " + CONFIGURATION_DIR);
            return;
        }

        // iterate over all configuration files in the folder
        for (final File fileEntry : configurationsFolder.listFiles()) {
            LOGGER.info("Reading configuration from file " + fileEntry.getName());

            String jsonContent = "";   // json configuration file
            try {
                BufferedReader read = new BufferedReader(new InputStreamReader(fileEntry.toURI().toURL().openStream()));

                String line;
                while ((line = read.readLine()) != null) {
                    jsonContent += line;
                }
            }
            catch(Exception e){
                LOGGER.error("Cannot read configuration from file " + fileEntry.getName(), e);
                continue;
            }

            // parsing of the json file content
            JsonNode rootNode = null;
            try {
                rootNode = JsonParser.parse(jsonContent);
            } catch (IOException e) {
                LOGGER.error("Cannot parse configuration from file " + fileEntry.getName());
                continue;
            }

            String configurationName = fileEntry.getName().split("\\.")[0];

            JsonNode arrayOfConfigurations = rootNode.get("configuration");

            if(arrayOfConfigurations == null)   // empty array
                continue;

            Map<String, Map<String, String>> newAntiPatternMap = new HashMap<>();

            for (int i = 0; i < arrayOfConfigurations.size(); i++) {

                JsonNode antiPatternNode = arrayOfConfigurations.get(i);

                String antiPatternName = antiPatternNode.get("antiPattern").asText();

                JsonNode arrayOfThresholds = antiPatternNode.get("thresholds");

                Map<String, String> newThresholds = new HashMap<String, String>();

                for (int j = 0; j < arrayOfThresholds.size(); j++) {
                    JsonNode thresholdNode = arrayOfThresholds.get(j);

                    String thresholdName = thresholdNode.get("thresholdName").asText();
                    String value = thresholdNode.get("value").asText();

                    newThresholds.put(thresholdName, value);
                }
                newAntiPatternMap.put(antiPatternName, newThresholds);

            }
            configurations.put(configurationName, newAntiPatternMap);
        }

        this.allConfigurations = configurations;

        LOGGER.info("-------FINISHED READING CONFIGURATIONS FROM FILES-------");
    }

    /**
     * Method that creates json file from the given configuration
     * @param configurationName Name of the json file
     * @param newConfiguration Map with configuration values
     */
    public void saveConfigurationToFile(String configurationName, Map<String, Map <String, String>> newConfiguration){
        ObjectNode root = JsonParser.getObjectMapper().createObjectNode();

        ArrayNode array = JsonParser.getObjectMapper().createArrayNode();

        for(String antiPatternName : newConfiguration.keySet()){
            ObjectNode antiPattern = JsonParser.getObjectMapper().createObjectNode();
            antiPattern.put("antiPattern", antiPatternName);

            ArrayNode thresholdsArray = JsonParser.getObjectMapper().createArrayNode();

            for(String thresholdName : newConfiguration.get(antiPatternName).keySet()){
                ObjectNode threshold = JsonParser.getObjectMapper().createObjectNode();
                threshold.put("thresholdName", thresholdName);
                threshold.put("value", newConfiguration.get(antiPatternName).get(thresholdName));
                thresholdsArray.add(threshold);
            }

            antiPattern.set("thresholds", thresholdsArray);
            array.add(antiPattern);
        }

        root.set("configuration", array);

        File configurationFile = null;
        try {
            configurationFile = new File(new FileSystemResource(CONFIGURATION_DIR + configurationName + ".json").getFile().getAbsolutePath());

            JsonParser.getObjectWriter().writeValue(configurationFile, root);
        } catch (Exception e) {
            LOGGER.error("Cannot write configuration to the file");
        }

    }

    /**
     * Method that saves new configuration from given parameters to the map structure
     * @param antiPatterns List of anti-pattern objects
     * @param configurationName New configuration name
     * @param antiPatternNames Array of anti-pattern names
     * @param thresholdNames Array of threshold names
     * @param thresholdValues Array pf threshold values
     * @param fullNewConfiguration True, if the whole new configuration is created and False, if only some thresholds are updated
     * @return List of the incorrectly set parameters
     */
    public List<String> saveNewConfiguration(List<AntiPattern> antiPatterns, String configurationName, String[] antiPatternNames, String[] thresholdNames, String[] thresholdValues, boolean fullNewConfiguration){
        Map<String, Map<String, String>> newConfiguration = new HashMap<>();
        List<String> incorrectParameters = new ArrayList<>();

        for(AntiPattern antiPattern : antiPatterns){
            if(antiPattern.getThresholds() == null)
                continue;

            for(int i = 0; i < thresholdNames.length; i++){
                if(antiPattern.getThresholds().containsKey(thresholdNames[i])){
                    if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("Integer")){
                        try {
                            Integer.parseInt(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("Percentage")){
                        try {
                            Percentage.parsePercentage(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("PositiveInteger")){
                        try {
                            PositiveInteger.parsePositiveInteger(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("PositiveFloat")){
                        try {
                            PositiveFloat.parsePositiveFloat(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("Float")){
                        try {
                            Float.parseFloat(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("Double")){
                        try {
                            Double.parseDouble(thresholdValues[i]);
                        }
                        catch(NumberFormatException e){
                            incorrectParameters.add(thresholdNames[i]);
                            LOGGER.error(e.getMessage());
                        }
                    }
                    else if(antiPattern.getThresholds().get(thresholdNames[i]).getType().equals("String")){
                        if (Utils.checkStringSubstrings(thresholdValues[i]) == false) {
                            incorrectParameters.add(thresholdNames[i]);
                        }
                    }
                }
            }
        }

        if(incorrectParameters.size() != 0)
            return incorrectParameters;

        if(fullNewConfiguration) { // creating full new configuration as we have all thresholds for all anti-patterns
            for (int i = 0; i < antiPatternNames.length; i++) {
                if (newConfiguration.get(antiPatternNames[i]) == null)
                    newConfiguration.put(antiPatternNames[i], new HashMap<>());

                newConfiguration.get(antiPatternNames[i]).put(thresholdNames[i], thresholdValues[i]);
            }
        }
        else{   // updating only some thresholds in current configuration
            newConfiguration = this.allConfigurations.get(configurationName);
            for(int i = 0; i < thresholdNames.length; i++) {
                newConfiguration.get(antiPatternNames[i]).replace(thresholdNames[i], thresholdValues[i]);
            }
        }


        if(this.allConfigurations.get(configurationName) == null)
            this.allConfigurations.put(configurationName, newConfiguration);
        else
            this.allConfigurations.replace(configurationName, newConfiguration);

        this.saveConfigurationToFile(configurationName, newConfiguration);

        return incorrectParameters;
    }
}
