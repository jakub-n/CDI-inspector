package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class ZoomInAction extends Action {

    private final InspectorPart inspectorPart;

    public ZoomInAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/add_correction.gif"));
        this.setToolTipText("Zoom in");
    }
    
    @Override
    public void run() {
        this.inspectorPart.zoomIn();
    }
}
