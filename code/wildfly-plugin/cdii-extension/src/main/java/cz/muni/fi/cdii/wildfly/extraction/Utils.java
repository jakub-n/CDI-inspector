package cz.muni.fi.cdii.wildfly.extraction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cz.muni.fi.cdii.common.model.Model;

public class Utils {
    
    public static String serialize(Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String serializedModel;
        try {
            serializedModel = objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            // TODO consistent exceptions management
            throw new RuntimeException("Model serialization failed.", e);
        }
        return serializedModel;
    }
}
