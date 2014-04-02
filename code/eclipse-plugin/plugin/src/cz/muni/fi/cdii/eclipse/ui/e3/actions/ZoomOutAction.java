package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class ZoomOutAction extends Action {

    private final InspectorPart inspectorPart;

    public ZoomOutAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(
                Activator.getImageDescriptor("icons/eclipse/remove_correction.gif"));
        this.setToolTipText("Zoom out");
    }
    
    @Override
    public void run() {
        this.inspectorPart.zoomOut();
    }
}
