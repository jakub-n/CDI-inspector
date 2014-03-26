package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;


public class ExpandAllAction extends Action {

    public ExpandAllAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/expandall.gif"));
        this.setToolTipText("Expand all nodes");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
