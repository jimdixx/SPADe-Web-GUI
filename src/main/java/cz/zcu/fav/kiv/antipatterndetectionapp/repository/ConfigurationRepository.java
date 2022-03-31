package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import com.fasterxml.jackson.databind.JsonNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigurationRepository implements ServletContextAware {

    private ServletContext servletContext;
    private static final String CONFIGURATION_DIR = "/configurations/";
    private final Logger LOGGER = LoggerFactory.getLogger(ConfigurationRepository.class);

    // Map<ConfigurationName, Map<AntiPatternName, Map<ThresholdName, value>>>
    public Map<String, Map<String, Map<String, String>>> allConfigurations;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;

        loadConfigurations();
    }

    private void loadConfigurations(){
        LOGGER.info("-------START READING CONFIGURATIONS FROM FILES-------");
        Map<String, Map<String, Map<String, String>>> configurations = new HashMap<>();
        try {
            URL url = servletContext.getResource(CONFIGURATION_DIR);
            File folder = new File(url.getFile());



            for (final File fileEntry : folder.listFiles()) {
                LOGGER.info("Reading configuration from file " + fileEntry.getName());
                String json = "";   // json configuration file
                JsonNode node = null;
                try {
                    json = new String(Files.readAllBytes(fileEntry.toPath()));
                    node = JsonParser.parse(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String configurationName = fileEntry.getName().split("\\.")[0];

                JsonNode arrayOfConfigurations = node.get("configuration");

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

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.allConfigurations = configurations;

        LOGGER.info("-------FINISHED READING CONFIGURATIONS FROM FILES-------");
    }
}
