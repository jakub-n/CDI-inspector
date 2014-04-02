package cz.muni.fi.cdii.eclipse.ui.graph;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;

public class CdiiGraphViewer extends GraphViewer {

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
}
