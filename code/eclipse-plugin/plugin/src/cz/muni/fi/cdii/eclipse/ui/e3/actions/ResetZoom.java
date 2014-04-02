package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class ResetZoom extends Action {

    private final InspectorPart inspectorPart;

    public ResetZoom(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/insp_sbook.gif"));
        this.setToolTipText("Reset zoom");
    }
    
    @Override
    public void run() {
        this.inspectorPart.resetZoom();
    }
}
