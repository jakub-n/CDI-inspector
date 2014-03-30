package cz.muni.fi.cdii.wildfly.extraction;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.muni.fi.cdii.common.model.Model;

public class Utils {
    
    public static String serialize(Model model) {
        
        return ObjectMapper.class.getCanonicalName() + " " + Model.class.getCanonicalName();
    }
}
