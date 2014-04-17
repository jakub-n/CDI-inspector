package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;

public class CdiiGraphContainer extends GraphContainer {

    public CdiiGraphContainer(Graph graph, int style) {
        super(graph, style);
        DirectedGraphLayoutAlgorithm directedGraphLayoutAlgorithm = 
                new DirectedGraphLayoutAlgorithm();
        directedGraphLayoutAlgorithm.setOrientation(DirectedGraphLayoutAlgorithm.HORIZONTAL);
        this.setLayoutAlgorithm(directedGraphLayoutAlgorithm, false);
    }
    
    /* tooltip can't be placed on title bar of the container only 
     * (org.eclipse.zest.core.widgets.GraphContainer.expandGraphLabel) due to encaplulation.
     * Tooltip on whole area of opened container is confusing
     */
//    @Override
//    protected void updateFigureForModel(IFigure currentFigure) {
//        super.updateFigureForModel(currentFigure);
//        if (this.getTooltip() != null) {
//            currentFigure.setToolTip(this.getTooltip());
//        }
//    }

    @Override
    public void close(boolean animate) {
        super.close(animate);
        List<GraphConnection> auxConnections = this.getAuxConnections();
        setConnectionsVisibility(auxConnections, true);
    }

    @Override
    public void open(boolean animate) {
        super.open(animate);
        List<GraphConnection> auxConnections = this.getAuxConnections();
        setConnectionsVisibility(auxConnections, false);
    }
    
    private void setConnectionsVisibility(List<GraphConnection> connections, boolean visible) {
        for (GraphConnection connection : connections) {
            connection.setVisible(visible);
        }
    }
    
    /**
     * 
     * @return connections that artificially added between this container and bean-node to visualize
     *         {@literal @}Inject or {@literal @}Produces relationship when the container is closed.
     *         Such connections carry {@link AuxiliaryConnectionMarker#INSTANCE} in its 'data' 
     *         property.  
     */
    private List<GraphConnection> getAuxConnections() {
        List<GraphConnection> sourceConnections = Utils.toCheckedList(this.getSourceConnections(), 
                GraphConnection.class);
        List<GraphConnection> targetConnections = Utils.toCheckedList(this.getTargetConnections(), 
                GraphConnection.class);
        List<GraphConnection> allConnections = new ArrayList<>();
        allConnections.addAll(sourceConnections);
        allConnections.addAll(targetConnections);
        List<GraphConnection> auxConnections = filterAuxiliaryConnections(allConnections);
        return auxConnections;
    }
    
    /**
     * 
     * @param connections input
     * @return subset of input connections 'data' property of which contains 
     *         {@link AuxiliaryConnectionMarker#INSTANCE} in its 'data' 
     *         property.  
     */
    private static List<GraphConnection> filterAuxiliaryConnections(
            List<GraphConnection> connections) {
        List<GraphConnection> result = new ArrayList<>();
        for (GraphConnection connection : connections) {
            if (AuxiliaryConnectionMarker.INSTANCE.equals(connection.getData())) {
                result.add(connection);
            }
        }
        return result;
    }
    
}
