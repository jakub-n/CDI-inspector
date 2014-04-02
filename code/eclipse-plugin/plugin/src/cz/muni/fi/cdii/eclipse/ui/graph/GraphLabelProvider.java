package cz.muni.fi.cdii.eclipse.ui.graph;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphMember;
import cz.muni.fi.cdii.eclipse.graph.model.GraphType;

public class GraphLabelProvider extends LabelProvider implements IEntityStyleProvider,
        ISelfStyleProvider {
    
    @Override
    public String getText(Object element) {
        if (element instanceof GraphBean) {
            GraphBean graphBean = (GraphBean) element;
            String result = graphBean.getOrigin().getNodeText();
            return result;
        }
        if (element instanceof GraphType) {
            GraphType graphType = (GraphType) element;
            String result = graphType.getOrigin().getNodeText();
            return result;
        }
        if (element instanceof GraphMember) {
            GraphMember graphMember = (GraphMember) element;
            String result = graphMember.getOrigin().getNodeText();
            return result;
        }
        return null;
    }

    @Override
    public void selfStyleConnection(Object element, GraphConnection connection) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Color getForegroundColour(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IFigure getTooltip(Object entity) {
        return null;
    }

    @Override
    public boolean fisheyeNode(Object entity) {
        return false;
    }

}
