package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class ConnectToServerAction extends Action {

    public ConnectToServerAction() {
        super();
        this.setImageDescriptor(
                Activator.getImageDescriptor("icons/eclipse/server_perspective.gif"));
        this.setToolTipText("Model from server");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
