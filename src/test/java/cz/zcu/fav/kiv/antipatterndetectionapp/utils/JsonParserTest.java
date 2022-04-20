package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonParserTest {

    @Test
    void testGetDefaultObjectMapper(){
        ObjectMapper objectMapper = JsonParser.getDefaultObjectMapper();
        assertEquals(objectMapper == null,false, "ObjectMapper was not initialized.");
    }

    @Test
    void testParse(){
        String jsonTest = "{\"test\":{\"threshold\":\"value\"}}";
        JsonNode nodeTest;
        try {
            nodeTest = JsonParser.parse(jsonTest);
            String result = nodeTest.get("test").get("threshold").asText();
            assertEquals(result, "value", "Json string was not parsed correctly.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetObjectWriter(){
        ObjectWriter objectWriter = JsonParser.getObjectWriter();
        assertEquals(objectWriter == null, false, "ObjectWriter was not initialized.");
    }

    @Test
    void testGetObjectMapper(){
        JsonParser.getDefaultObjectMapper();
        ObjectMapper objectMapper = JsonParser.getObjectMapper();
        assertEquals(objectMapper == null, false, "Object mapper was not returned correctly.");
    }
}
