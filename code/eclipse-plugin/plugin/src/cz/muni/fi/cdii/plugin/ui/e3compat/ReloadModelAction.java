package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class ReloadModelAction extends Action {

    public ReloadModelAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/refresh.gif"));
        this.setToolTipText("Reload model");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
