package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import com.fasterxml.jackson.databind.JsonNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Threshold;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * A class that takes care of working with AP.
 */
@Component
public class AntiPatternRepository implements ServletContextAware {

    private static final String QUERY_DIR = "/queries/";
    private static final String AP_DIR = "/antipatterns/";
    private static final String OPERATIONALIZATION_DIR = "/operationalizations/";
    private static final String OPERATIONALIZATION_IMG_DIR = "/operationalizations/images/";
    private final Logger LOGGER = LoggerFactory.getLogger(AntiPatternRepository.class);
    private ServletContext servletContext;
    private Map<Long, AntiPatternDetector> antiPatternDetectors;

    /**
     * This method load all queries from files for each AP.
     *
     * @param servletContext servlet context
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

        // initialize all detectors
        this.antiPatternDetectors = initDetectors();

        // initialize sql queries
        initSqlQueries();
    }


    /**
     * The method that loads all available detectors that implement
     * the AntiPatternDetector interface creates objects that the
     * application continues to work with. Detection of all interface
     * implementations is performed using reflection.
     *
     * @return map of all AP
     */
    private Map<Long, AntiPatternDetector> initDetectors() {
        LOGGER.info("-------START CREATING DETECTORS WITH REFLECTION-------");

        Map<Long, AntiPatternDetector> antiPatterns = new HashMap<>();
        try {
            Reflections reflections = new Reflections("cz.zcu.fav.kiv.antipatterndetectionapp");
            Set<Class<? extends AntiPatternDetector>> subTypes = reflections.getSubTypesOf(AntiPatternDetector.class);
            for (Class<? extends AntiPatternDetector> subType : subTypes) {
                AntiPatternDetector antiPatternDetector = subType.getDeclaredConstructor().newInstance();

                // loading anti-pattern from json file and linking it to the detector file
                antiPatternDetector.setAntiPattern(getAntiPatternFromJsonFile(antiPatternDetector.getJsonFileName()));

                antiPatterns.putIfAbsent(antiPatternDetector.getAntiPatternModel().getId(), antiPatternDetector);
                LOGGER.info("Creating detector " + antiPatternDetector.getAntiPatternModel().getPrintName());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            LOGGER.error("Cannot get all detectors with reflection", e);
        }

        LOGGER.info("-------FINISHED CREATING DETECTORS WITH REFLECTION-------");

        return antiPatterns;
    }

    /**
     * Method for reading SQL queries from files
     */
    private void initSqlQueries(){
        LOGGER.info("-------START READING SQL FROM FILES-------");

        for (AntiPatternDetector antiPatternDetector : getAllAntiPatterns()) {
            LOGGER.info("Reading sql files for AP " + antiPatternDetector.getAntiPatternModel().getPrintName());
            antiPatternDetector.setSqlQueries(loadSqlFile(antiPatternDetector.getSqlFileNames()));
        }

        LOGGER.info("-------FINISHED READING SQL FROM FILES-------");
    }


    /**
     * Method for loading list of sql files from given list of files
     *
     * @param fileNames list of files with sql queries
     * @return list of queries
     */
    private List<String> loadSqlFile(List<String> fileNames) {
        List<String> queries = new ArrayList<>();

        // walk through all sql filenames and load all sql queries
        for (String fileName : fileNames) {

            LOGGER.info("Reading sql query from file " + fileName);

            try {
                URL test = servletContext.getResource(QUERY_DIR + fileName);
                BufferedReader read = new BufferedReader(
                        new InputStreamReader(test.openStream()));
                String line;
                while ((line = read.readLine()) != null) {
                    if (line.startsWith("select") || line.startsWith("set") && line.charAt(line.length() - 1) == ';') {
                        queries.add(line);
                    }
                }
                read.close();
            } catch (IOException e) {
                LOGGER.error("Cannot read sql from file " + fileName);
                return queries;
            }
        }
        return queries;
    }

    /**
     * Method for reading anti-pattern information from json files
     *
     * @param jsonFileName Name of the file
     * @return AntiPattern object
     */
    public AntiPattern getAntiPatternFromJsonFile(String jsonFileName){
        String json = "";   // json configuration file
        JsonNode node = null;

        LOGGER.info("Reading anti-pattern from json file " + jsonFileName);

        try {
            URL url = servletContext.getResource(AP_DIR + jsonFileName);
            BufferedReader read = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String line;
            while ((line = read.readLine()) != null) {
                json += line;
            }
            node = JsonParser.parse(json);
        } catch (IOException e) {
            LOGGER.warn("Cannot read anti-pattern from json file " + jsonFileName);
            return null;
        }

        Long APid = Long.parseLong(node.get("id").asText());
        String APPrintName = node.get("printName").asText();
        String APName = node.get("name").asText();
        String APDescription = node.get("description").asText();
        String APCatalogueFileName = node.get("catalogueFileName") != null ? node.get("catalogueFileName").asText() : null;

        Map<String, Threshold> APMap = new HashMap<>();

        JsonNode array = node.get("thresholds");
        Threshold tmpThreshold = null;

        for(int i = 0; i < array.size(); i++){
            JsonNode tmpNode = array.get(i);

            String thresholdName = tmpNode.get("thresholdName").asText();
            String thresholdType = tmpNode.get("thresholdType").asText();

            JsonNode thresholdNode = tmpNode.get("threshold");

            String tThresholdName = thresholdNode.get("thresholdName").asText();
            String tThresholdPrintName = thresholdNode.get("thresholdPrintName").asText();
            String tThresholdDescription = thresholdNode.get("thresholdDescription").asText();
            String tThresholdErrorMess = thresholdNode.get("thresholdErrorMess").asText();

            tmpThreshold = getThreshold(thresholdType, tThresholdName, tThresholdPrintName, tThresholdDescription, tThresholdErrorMess);

            APMap.put(thresholdName, tmpThreshold);
        }

        AntiPattern newAP = new AntiPattern(APid, APPrintName, APName, APDescription, APMap, APCatalogueFileName);

        return newAP;
    }

    /**
     * Get all loaded AP from global value.
     *
     * @return list of AP
     */
    public List<AntiPatternDetector> getAllAntiPatterns() {
        return new ArrayList<>(this.antiPatternDetectors.values());
    }

    /**
     * Get AP by given ID.
     *
     * @param id AP ID
     * @return AP
     */
    public AntiPatternDetector getAntiPatternById(Long id) {
        return this.antiPatternDetectors.getOrDefault(id, null);
    }

    /**
     * Method for creating of the Threshold object from given parameters
     *
     * @param thresholdType Type of the threshold
     * @param name Threshold name
     * @param printName Threshold name for print
     * @param description Short description of the threshold
     * @param errorMessage Error message to display if threshold's value is set wrong
     * @return Threshold object
     */
    private Threshold getThreshold(String thresholdType, String name, String printName, String description, String errorMessage){

        if(thresholdType.equals("Percentage") || thresholdType.equals("PositiveFloat") || thresholdType.equals("PositiveInteger") || thresholdType.equals("String")){
            return new Threshold(name, printName, description, errorMessage, thresholdType);
        }

        return null;
    }

    public String getOperationalizationDirPathName(){
        try {
            return servletContext.getResource(OPERATIONALIZATION_DIR).getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOperationalizationImgDirFileName(){
        try {
            return servletContext.getResource(OPERATIONALIZATION_IMG_DIR).getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
