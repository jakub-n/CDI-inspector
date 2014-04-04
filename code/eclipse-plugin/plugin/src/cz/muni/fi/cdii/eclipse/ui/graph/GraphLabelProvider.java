package cz.muni.fi.cdii.eclipse.ui.graph;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
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
    
    public GraphLabelProvider(ColorManager colorManager) {
        this.colorManager = colorManager;
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
//        connection.setWeight(weight);(weight);
        ConnectionType connectionType = classifyConnection(connection);
        switch (connectionType) {
        case MAIN_TYPE:
            setConnectionColor(connection, GraphColorEnum.TYPE_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_SOLID);
            connection.setWeight(1);
            connection.setTooltip(new Label("type"));
            break;
        case INJECT:
            setConnectionColor(connection, GraphColorEnum.INJECT_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
            connection.setWeight(0.2);
            connection.setTooltip(new Label("@inject"));
            break;
        case PRODUCES:
            setConnectionColor(connection, GraphColorEnum.PRODUCES_CONNECTION);
            connection.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
            connection.setWeight(0.2);
            connection.setTooltip(new Label("@produces"));
            break;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Color getBorderColor(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Color getBorderHighlightColor(Object entity) {
        // TODO Auto-generated method stub
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
    
    private static ConnectionType classifyConnection(GraphConnection connection) {
        Object sourceElement = connection.getSource().getData();
        Object destinationElement = connection.getDestination().getData();
        if (sourceElement instanceof GraphMember && destinationElement instanceof GraphBean) {
            return ConnectionType.PRODUCES;
        }
        if (sourceElement instanceof GraphBean && destinationElement instanceof GraphMember) {
            return ConnectionType.INJECT;
        }
        if (sourceElement instanceof GraphBean && destinationElement instanceof GraphType) {
            return ConnectionType.MAIN_TYPE;
        }
        throw new RuntimeException("Unknown connection type");
    }
    
    private static enum ConnectionType {
        MAIN_TYPE,
        PRODUCES,
        INJECT
    }
    
}
