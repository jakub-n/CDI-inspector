package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class CollapseAllAction extends Action {

    public CollapseAllAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/collapseall.gif"));
        this.setToolTipText("Collapse all node");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
