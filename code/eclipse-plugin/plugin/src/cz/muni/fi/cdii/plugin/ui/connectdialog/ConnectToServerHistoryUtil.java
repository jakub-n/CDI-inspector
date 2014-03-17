package cz.muni.fi.cdii.plugin.ui.connectdialog;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import cz.muni.fi.cdii.plugin.Activator;

public class ConnectToServerHistoryUtil {

    private static final String QUALIFIER = Activator.PLUGIN_ID + ".connectionHistory";
    private static final String KEY = "historyJsonArray";
    private static final String JSON_EMTPY_ARRAY = "[]";
    private static final int HISTORY_LENGTH = 8;
    
    public static String[] getHistory() {
        String jsonArray = InstanceScope.INSTANCE.getNode(QUALIFIER).get(KEY, JSON_EMTPY_ARRAY);
        String[] result = jsonParse(jsonArray);
        return result;
    }
    
    private static String[] jsonParse(String jsonArray) {
        try {
            return jsonParseUnchecked(jsonArray);
        } catch (IOException ex) {
            return new String[0];
        }
    }
    
    private static String[] jsonParseUnchecked (String jsonArray) 
            throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode arrayNode = objectMapper.readTree(jsonArray);
        List<String> result = new ArrayList<>();
        for (JsonNode stringNode : arrayNode) {
            result.add(stringNode.asText());
        }
        return result.toArray(new String[0]);
    }

    public static void saveLast(String lastElement) {
        List<String> history = new ArrayList<>();
        history.add(lastElement);
        String[] storedHistory = getHistory();
        for (String historyItem : storedHistory) {
            if (!history.contains(historyItem)) {
                history.add(historyItem);
            }
        }
        String[] historyArray = history.toArray(new String[0]);
        if (historyArray.length > HISTORY_LENGTH) {
            historyArray = Arrays.copyOf(storedHistory, HISTORY_LENGTH);
        }
        save(historyArray);
    }

    private static void save(String[] history) {
        String jsonHistory = createJsonString(history);
        saveToPreferencesNode(jsonHistory);
    }

    private static String createJsonString(String[] history) {
        try {
            return createJsonHistoryUnchecked(history);
        } catch (IOException ex) {
            return "[]";
        }
    }

    private static String createJsonHistoryUnchecked(String[] history)
            throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String element : history) {
            arrayNode.add(element);
        }
        
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(charArrayWriter);
        objectMapper.writeTree(generator, arrayNode);
        String jsonHistory = charArrayWriter.toString();
        return jsonHistory;
    }

    private static void saveToPreferencesNode(String stringToSAve) {
        try {
            IEclipsePreferences preferencesNode = InstanceScope.INSTANCE.getNode(QUALIFIER);
            preferencesNode.put(KEY, stringToSAve);
            preferencesNode.flush();
        } catch (BackingStoreException ex) {
            // nothing
        }
    }
}
