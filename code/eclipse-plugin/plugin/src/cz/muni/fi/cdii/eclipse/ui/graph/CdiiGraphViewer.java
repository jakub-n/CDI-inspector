package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;

public class CdiiGraphViewer extends GraphViewer {
    
    private Set<CdiiGraphContainer> containers;

    public CdiiGraphViewer(Composite composite, int style) {
        super(composite, style);
    }
    
    public void zoomIn() {
        this.getZoomManager().zoomIn();
    }
    
    public void zoomOut() {
        this.getZoomManager().zoomOut();
    }
    
    public void resetZoom() {
        this.getZoomManager().setZoom(1.0);
    }
    
    public void openAllContainer() {
        for (GraphContainer container : this.containers) {
            container.open(true);
        }
    }
    
    public void closeAllContainer() {
        for (GraphContainer container : this.containers) {
            container.close(true);
        }
    }
    
    /**
     * Ensures that custom {@link CdiiGraphContainer} will be used instead standard 
     * {@link GraphContainer}
     * <br>
     * Code does not call super method.
     */
    @SuppressWarnings("unchecked")
    @Override
    public GraphNode addGraphModelContainer(Object element) {
        GraphNode node = this.getGraphModelNode(element);
        if (node == null) {
            CdiiGraphContainer container = new CdiiGraphContainer((Graph) getControl(), SWT.NONE);
            this.containers.add(container);
            
            node = container;
            this.getNodesMap().put(element, node);
            node.setData(element);
        }
        return node;
    }
    
    @Override
    protected void inputChanged(Object input, Object oldInput) {
        this.containers = new HashSet<>();
        super.inputChanged(input, oldInput);
    }
}
