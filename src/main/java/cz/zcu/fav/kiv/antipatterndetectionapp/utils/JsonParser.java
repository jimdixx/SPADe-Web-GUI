package cz.zcu.fav.kiv.antipatterndetectionapp.utils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public class JsonParser {

    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    /**
     * Method for getting Jackson ObjectMapper object
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defObjectMapper = new ObjectMapper();

        /* possible modification of ObjectMapper could be placed here */

        return defObjectMapper;
    }

    /**
     * Method for reading tree model from json
     *
     * @param src
     * @return JsonNode
     * @throws IOException
     */
    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    public static ObjectWriter getObjectWriter() {
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        return writer;
    }

    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }
}
