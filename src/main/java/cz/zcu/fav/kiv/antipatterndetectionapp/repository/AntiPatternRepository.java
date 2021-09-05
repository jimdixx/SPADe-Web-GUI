package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
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
import java.net.URL;
import java.util.*;

@Component
public class AntiPatternRepository implements ServletContextAware {

    private static final String QUERY_DIR = "/queries/";
    private final Logger LOGGER = LoggerFactory.getLogger(AntiPatternRepository.class);
    private ServletContext servletContext;
    private Map<Long, AntiPatternDetector> antiPatternDetectors = init();

    private Map<Long, AntiPatternDetector> init() {
        LOGGER.info("-------START CREATING DETECTORS WITH REFLECTION-------");
        Map<Long, AntiPatternDetector> antiPatterns = new HashMap<>();
        try {
            Reflections reflections = new Reflections("cz.zcu.fav.kiv.antipatterndetectionapp");
            Set<Class<? extends AntiPatternDetector>> subTypes = reflections.getSubTypesOf(AntiPatternDetector.class);
            for (Class<? extends AntiPatternDetector> subType : subTypes) {
                AntiPatternDetector antiPatternDetector = subType.getDeclaredConstructor().newInstance();
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

    public List<AntiPatternDetector> getAllAntiPatterns() {
        return new ArrayList<>(this.antiPatternDetectors.values());
    }

    public AntiPatternDetector getAntiPatternById(Long id) {
        return this.antiPatternDetectors.getOrDefault(id, null);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        LOGGER.info("-------START READING SQL FROM FILES-------");
        for (AntiPatternDetector antiPatternDetector : getAllAntiPatterns()) {
            LOGGER.info("Reading sql files for AP " + antiPatternDetector.getAntiPatternModel().getPrintName());
            antiPatternDetector.setSqlQueries(loadSqlFile(antiPatternDetector.getSqlFileNames()));

        }
        LOGGER.info("-------FINISHED READING SQL FROM FILES-------");
    }

    /**
     * Method for loading list of sql files from given list of files
     * @param fileNames list of files with sql queries
     * @return list of queries
     */
    private List<String> loadSqlFile(List<String> fileNames) {
        List<String> queries = new ArrayList<>();

        // walk through all sql filenames and load all sql queries
        for (String fileName : fileNames) {
            LOGGER.info("Reading sql query from file name " + fileName);
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
                LOGGER.warn("Cannot read sql from file " + fileName, e);
                return queries;
            }
        }
        return queries;
    }
}
