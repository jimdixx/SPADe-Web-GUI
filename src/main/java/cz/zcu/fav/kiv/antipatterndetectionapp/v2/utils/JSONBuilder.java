package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

/**
 * Version 1.0 - only simple json can be built
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
public class JSONBuilder {

    /**
     * Method transforms map into string representation of JSON object
     * @param map key value pair that will be generated as json line
     * @return String representation of JSON object
     */
    public static String buildJson(HashMap<String, String> map){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonObject = mapper.createObjectNode();
        for (String key : map.keySet()) {
            jsonObject.put(key,map.get(key));
        }
        return jsonObject.toString();
    }



}
