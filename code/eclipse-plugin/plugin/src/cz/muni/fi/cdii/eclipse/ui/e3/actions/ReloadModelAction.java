package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class ReloadModelAction extends Action {
    
    final InspectorPart inspectorPart;

    public ReloadModelAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/refresh.gif"));
        this.setToolTipText("Reload data");
    }
    
    @Override
    public void run() {
        this.inspectorPart.reinspect();
    }
}
