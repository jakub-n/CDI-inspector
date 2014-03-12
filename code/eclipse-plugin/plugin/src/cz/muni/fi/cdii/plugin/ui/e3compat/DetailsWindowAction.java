package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class DetailsWindowAction extends Action {

    public DetailsWindowAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/properties.gif"));
        this.setToolTipText("Show details window");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
