package cz.muni.fi.cdii.eclipse;

/**
 * List of all used e4 event topics
 *
 */
public interface CdiiEventTopics {

    public static final String INSPECT = "cz/muni/fi/cdii/eclipse/events/inspect";
    public static final String UPDATE_FILTER_LABELS = 
            "cz/muni/fi/cdii/eclipse/events/updateFilterLabels";
    public static final String UPDATE_DETAILS = "cz/muni/fi/cdii/eclipse/events/updateDetails";
    public static final String FILTER_GRAPH = "cz/muni/fi/cdii/eclipse/events/filterGraph";
    public static final String SELECT_NODE = "cz/muni/fi/cdii/eclipse/events/selectNode";
    public static final String UPDATE_DETAILS_REQUEST = 
            "cz/muni/fi/cdii/eclipse/events/updateDetailsRequest";
    
}
