package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class ZoomOutAction extends Action {

    public ZoomOutAction() {
        super();
        this.setImageDescriptor(
                Activator.getImageDescriptor("icons/eclipse/remove_correction.gif"));
        this.setToolTipText("Zoom out");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
