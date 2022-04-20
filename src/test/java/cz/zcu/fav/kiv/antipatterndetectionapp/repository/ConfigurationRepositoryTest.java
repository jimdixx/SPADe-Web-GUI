package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Threshold;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationRepositoryTest {

    ConfigurationRepository configurationRepository;

    @BeforeEach
    void setUp(){
        configurationRepository = new ConfigurationRepository("data/");
    }

    @Test
    void testSaveNewConfiguration(){
        // creating test threshold
        Threshold threshold = new Threshold("test", "test", "test", "test", "Integer");
        Map<String, Threshold> thresholdMap = new HashMap<>();
        thresholdMap.put("testThreshold", threshold);

        // creating test anti-pattern
        AntiPattern antiPattern = new AntiPattern(1L, "test", "test", "test", thresholdMap);
        List<AntiPattern> antiPatternList = new ArrayList<>();
        antiPatternList.add(antiPattern);
        String[] antiPatternNames ={"test"};
        String[] thresholdNames ={"testThreshold"};
        String[] thresholdValues ={"1"};

        // testing of new configuration save
        List<String> resultOfSave = configurationRepository.saveNewConfiguration(antiPatternList, "test/testConfiguration", antiPatternNames, thresholdNames, thresholdValues, true);

        // checking results
        assertEquals(0, resultOfSave.size(), "Configuration was not set correctly.");
        assertEquals(1, configurationRepository.allConfigurations.get("test/testConfiguration").size(), "Configuration was not set correctly.");
        assertEquals("1", configurationRepository.allConfigurations.get("test/testConfiguration").get("test").get("testThreshold"), "Configuration was not set correctly.");

        // testing of configuration update
        thresholdValues[0] = "2";
        configurationRepository.saveNewConfiguration(antiPatternList, "test/testConfiguration", antiPatternNames, thresholdNames, thresholdValues, false);

        // checking results
        assertEquals(1, configurationRepository.allConfigurations.get("test/testConfiguration").size(), "Configuration was not set correctly.");
        assertEquals("2", configurationRepository.allConfigurations.get("test/testConfiguration").get("test").get("testThreshold"), "Configuration was not set correctly.");

        // testing of configuration wrong threshold value update
        thresholdValues[0] = "test";
        List<String> resultOfUpdate = configurationRepository.saveNewConfiguration(antiPatternList, "test/testConfiguration", antiPatternNames, thresholdNames, thresholdValues, false);
        assertEquals(1, resultOfUpdate.size(), "Configuration was not set correctly.");
    }

    @Test
    void testSaveConfigurationToFile(){
        // preparing configuration to save
        String testName = "test/test";
        Map<String, Map<String, String>> testConfiguration = new HashMap<>();
        Map<String, String> testAntiPattern = new HashMap<>();

        testAntiPattern.put("testThreshold", "testValue");
        testConfiguration.put("testAntiPattern", testAntiPattern);
        new File(new FileSystemResource("data/configurations/test/").getFile().getAbsolutePath()).mkdir();

        // saving configuration
        configurationRepository.saveConfigurationToFile(testName, testConfiguration);

        // checking result json file
        File testConfigFile = new File(new FileSystemResource("data/configurations/test/test.json").getFile().getAbsolutePath());

        String jsonContent = "";   // json configuration file content
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(testConfigFile.toURI().toURL().openStream()));

            String line;
            while ((line = read.readLine()) != null) {
                jsonContent += line;
            }
            read.close();
        }
        catch(Exception e){}

        JsonNode rootNode = null;
        try {
            rootNode = JsonParser.parse(jsonContent);
        } catch (IOException e) {}

        JsonNode configurations = rootNode.get("configuration");
        JsonNode thresholds = configurations.get(0).get("thresholds");

        // checking results
        assertEquals("testAntiPattern", configurations.get(0).get("antiPattern").asText());
        assertEquals("testThreshold",  thresholds.get(0).get("thresholdName").asText());
        assertEquals("testValue", thresholds.get(0).get("value").asText());

        // deleting test temp file
        try {
            FileUtils.forceDelete(new File(new FileSystemResource("data/configurations/test").getFile().getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testConfigurationRepository(){
        for(String configuration : configurationRepository.allConfigurations.keySet()){
            assertEquals(configurationRepository.allConfigurations.get(configuration) == null, false, "Configuration was not loaded correctly.");
        }
    }
}
