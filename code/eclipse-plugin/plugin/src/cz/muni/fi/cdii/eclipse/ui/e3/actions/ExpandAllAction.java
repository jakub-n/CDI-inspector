package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class ExpandAllAction extends Action {

    private final InspectorPart inspectorPart;

    public ExpandAllAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/expandall.gif"));
        this.setToolTipText("Expand all nodes");
    }
    
    @Override
    public void run() {
        inspectorPart.expandAllNodes();
    }
}
