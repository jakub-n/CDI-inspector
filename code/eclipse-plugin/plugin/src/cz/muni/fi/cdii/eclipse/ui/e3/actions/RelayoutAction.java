package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;


public class RelayoutAction extends Action {

    public RelayoutAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/dep_loop.gif"));
        this.setToolTipText("Relayout graph");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
