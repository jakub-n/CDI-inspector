package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;


public class RelayoutAction extends Action {

    private final InspectorPart inspectorPart;

    public RelayoutAction(InspectorPart inspectorPart) {
        super();
        this.inspectorPart = inspectorPart;
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/dep_loop.gif"));
        this.setToolTipText("Relayout graph");
    }
    
    @Override
    public void run() {
        this.inspectorPart.relayout();
    }
}
