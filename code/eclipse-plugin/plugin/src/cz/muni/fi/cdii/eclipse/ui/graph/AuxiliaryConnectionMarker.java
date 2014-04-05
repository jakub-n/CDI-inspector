package cz.muni.fi.cdii.eclipse.ui.graph;

import org.eclipse.zest.core.widgets.GraphConnection;

/**
 * Singleton marking auxiliary graph connections that represents {@literal @}Inject and 
 * {@literal @}Produces relations in graph when Type-container is close.
 * <br>
 * Auxiliary {@link GraphConnection}s carry this marker in 'data' property.
 */
public class AuxiliaryConnectionMarker {
    
    public static AuxiliaryConnectionMarker INSTANCE = new AuxiliaryConnectionMarker();

    private AuxiliaryConnectionMarker() {
    }
}
