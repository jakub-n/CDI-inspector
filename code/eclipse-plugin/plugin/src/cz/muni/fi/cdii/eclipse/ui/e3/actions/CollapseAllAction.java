package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class CollapseAllAction extends Action {

    private final InspectorPart inspectorPart;

    public CollapseAllAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/collapseall.gif"));
        this.setToolTipText("Collapse all node");
    }
    
    @Override
    public void run() {
        this.inspectorPart.collapseAllNode();
    }
}
