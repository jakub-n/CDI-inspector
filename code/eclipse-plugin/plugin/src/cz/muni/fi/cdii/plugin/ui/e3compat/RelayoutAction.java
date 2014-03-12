package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


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
