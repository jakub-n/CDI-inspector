package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import cz.muni.fi.cdii.common.model.Field;
import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphElement;
import cz.muni.fi.cdii.eclipse.graph.model.GraphMember;
import cz.muni.fi.cdii.eclipse.graph.model.GraphType;
import cz.muni.fi.cdii.eclipse.ui.graph.ColorManager.GraphColorEnum;

public class GraphLabelProvider extends LabelProvider implements IEntityStyleProvider,
        ISelfStyleProvider {
    
    final private ColorManager colorManager;
    final private GraphViewer graphViewer;
    
    public GraphLabelProvider(ColorManager colorManager, GraphViewer graphViewer) {
        this.colorManager = colorManager;
        this.graphViewer = graphViewer;
    }

    @Override
    public String getText(Object element) {
        if (element instanceof GraphElement) {
            GraphElement graphElement = (GraphElement) element;
            String result = graphElement.getOrigin().getNodeText();
            return result;
        }
        return null;
    }

    @Override
    public void selfStyleConnection(Object element, GraphConnection connection) {
        ConnectionType connectionType = classifyConnection(connection);
        switch (connectionType) {
        case MAIN_TYPE:
            setConnectionColor(connection, GraphColorEnum.TYPE_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
            connection.setTooltip(new Label("type"));
            break;
        case INJECT:
        case AUX_INJECT:
            setConnectionColor(connection, GraphColorEnum.INJECT_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
            connection.setTooltip(new Label("@Inject"));
            break;
        case PRODUCES:
        case AUX_PRODUCES:
            setConnectionColor(connection, GraphColorEnum.PRODUCES_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
            connection.setTooltip(new Label("@Produces"));
            break;
        }
        
        switch (connectionType) {
        case AUX_INJECT:
        case AUX_PRODUCES:
            connection.setVisible(false);
            break;
        case MAIN_TYPE:
        case INJECT:
        case PRODUCES:
        }
    }

    private void setConnectionColor(GraphConnection connection, 
            ColorManager.GraphColorEnum colorName) {
        connection.setLineColor(this.colorManager.getNamedColor(colorName));
        connection.setHighlightColor(this.colorManager.getNamedColor(colorName));
    }

    @Override
    public void selfStyleNode(Object element, GraphNode node) {
    }

    @Override
    public Color getNodeHighlightColor(Object entity) {
        return null;
    }

    @Override
    public Color getBorderColor(Object entity) {
        return null;
    }

    @Override
    public Color getBorderHighlightColor(Object entity) {
        return null;
    }

    @Override
    public int getBorderWidth(Object entity) {
        return 1;
    }

    @Override
    public Color getBackgroundColour(Object entity) {
        NodeType nodeType = classifyNode(entity);
        switch (nodeType) {
        case TYPE:
            return this.colorManager.getNamedColor(GraphColorEnum.TYPE_NODE);
        case BEAN:
            return this.colorManager.getNamedColor(GraphColorEnum.BEAN_NODE);
        case FIELD:
            return this.colorManager.getNamedColor(GraphColorEnum.FIELD_NODE);
        case METHOD:
            return this.colorManager.getNamedColor(GraphColorEnum.METHOD_NODE);
        }
        return null;
    }

    @Override
    public Color getForegroundColour(Object entity) {
        return this.colorManager.getNamedColor(GraphColorEnum.FONT);
    }

    @Override
    public IFigure getTooltip(Object entity) {
        if (entity instanceof GraphElement) {
            GraphElement graphElement = (GraphElement) entity;
            String tooltipText = graphElement.getOrigin().getNodeTooltipText();
            Label label = new Label(tooltipText);
            return label;
        }
        return null;
    }

    @Override
    public boolean fisheyeNode(Object entity) {
        return false;
    }
    
    private static NodeType classifyNode(Object graphEntity) {
        if (graphEntity instanceof GraphType) {
            return NodeType.TYPE;
        }
        if (graphEntity instanceof GraphBean) {
            return NodeType.BEAN;
        }
        if (graphEntity instanceof GraphMember) {
            GraphMember graphMember = (GraphMember) graphEntity;
            Member origin = graphMember.getOrigin();
            if (origin instanceof Field) {
                return NodeType.FIELD;
            } else {
                return NodeType.METHOD;
            }
        }
        throw new RuntimeException("Unknown node entity.");
    }
    
    private static enum NodeType {
        TYPE,
        BEAN,
        METHOD,
        FIELD
    }
    
    /**
     * Classifies connection and if it is auxiliary, sets {@link AuxiliaryConnectionMarker#INSTANCE}
     * marker
     * @param connection to determine the type of
     * @return
     */
    private ConnectionType classifyConnection(GraphConnection connection) {
        Object sourceElement = connection.getSource().getData();
        Object destinationElement = connection.getDestination().getData();
        if (sourceElement instanceof GraphMember && destinationElement instanceof GraphBean) {
            return ConnectionType.PRODUCES;
        }
        if (sourceElement instanceof GraphBean && destinationElement instanceof GraphMember) {
            return ConnectionType.INJECT;
        }
        if (sourceElement instanceof GraphType && destinationElement instanceof GraphBean) {
            connection.setData(AuxiliaryConnectionMarker.INSTANCE);
            return ConnectionType.AUX_PRODUCES;
        }
        if (sourceElement instanceof GraphBean && destinationElement instanceof GraphType) {
            if (AuxiliaryConnectionMarker.INSTANCE.equals(connection.getData())) {
                return ConnectionType.AUX_INJECT;
            }
            GraphBean bean = (GraphBean) sourceElement;
            GraphType type = (GraphType) destinationElement;
            if (!bean.getMainType().equals(type)) {
                connection.setData(AuxiliaryConnectionMarker.INSTANCE);
                return ConnectionType.AUX_INJECT;
            }
            return resolveBeanToTypeEdge(bean, type, connection);
        }
        throw new RuntimeException("Unknown connection type");
    }
    
    /**
     * There could be from one to two (or more in case of wrong data collection) edges form bean to 
     * type node. Either 'mainType' edge only, or
     * 'mainType' and 'auxInclude'. Information about which is which is lost in 
     * {@link IGraphEntityContentProvider} instance.
     * <p>
     * If this is the case, his method randomly pick one and mark is using 
     * {@link AuxiliaryConnectionMarker#INSTANCE} to be the auxiliary one.
     * @param bean source bean
     * @param type destination type
     * @param connection type of which should be determined
     * @return type of connection currently asked 
     */
    private ConnectionType resolveBeanToTypeEdge(GraphBean bean, GraphType type, 
            GraphConnection connection) {
        GraphNode targetTypeNode = (GraphNode) this.graphViewer.findGraphItem(type);
        List<GraphConnection> connectionsToType = 
                Utils.toCheckedList(targetTypeNode.getTargetConnections(), GraphConnection.class);
        Set<GraphConnection> connectionsFromBeanToTheType = filterConnectionsFromBean(bean,
                connectionsToType);
        if (connectionsFromBeanToTheType.size() <= 1) {
            return ConnectionType.MAIN_TYPE;
        }
        return resolveBeanToTypeConnections(connectionsFromBeanToTheType, connection);
    }

    private Set<GraphConnection> filterConnectionsFromBean(GraphBean bean,
            List<GraphConnection> connections) {
        Set<GraphConnection> result = new HashSet<>();
        for (GraphConnection connection : connections) {
            if (bean.equals(connection.getSource().getData())) {
                result.add(connection);
            }
        }
        return result;
    }

    private ConnectionType resolveBeanToTypeConnections(Set<GraphConnection> connections, 
            GraphConnection connection) {
        if (!connections.contains(connection)) {
            throw new IllegalArgumentException("Connection is not contained in provided set.");
        }
        GraphConnection auxConnection = getAuxiliaryMarkedConnection(connections);
        if (auxConnection == null) {
            connection.setData(AuxiliaryConnectionMarker.INSTANCE);
            return ConnectionType.AUX_INJECT;
        }
        if (auxConnection.equals(connection)) {
            return ConnectionType.AUX_INJECT;
        }
        return ConnectionType.MAIN_TYPE;
    }

    private GraphConnection getAuxiliaryMarkedConnection(Set<GraphConnection> connections) {
        for (GraphConnection connection : connections) {
            if (AuxiliaryConnectionMarker.INSTANCE.equals(connection.getData())) {
                return connection;
            }
        }
        return null;
    }

    private static enum ConnectionType {
        /**
         * bean -> type
         */
        MAIN_TYPE,
        /**
         * member -> bean
         */
        PRODUCES,
        /**
         * bean -> member
         */
        INJECT,
        /**
         * type -> bean
         */
        AUX_PRODUCES,
        /**
         * bean-> type
         */
        AUX_INJECT
    }
    
}
