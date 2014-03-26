package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;


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
