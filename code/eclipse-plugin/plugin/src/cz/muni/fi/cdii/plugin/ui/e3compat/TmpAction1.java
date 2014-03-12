package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class TmpAction1 extends Action {

    public TmpAction1() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/action1.gif"));
        this.setToolTipText("tmp action 1");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
