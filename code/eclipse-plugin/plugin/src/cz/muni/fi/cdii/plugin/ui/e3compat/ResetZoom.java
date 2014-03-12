package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class ResetZoom extends Action {

    public ResetZoom() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/insp_sbook.gif"));
        this.setToolTipText("Reset zoom");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
