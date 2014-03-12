package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.plugin.Activator;


public class FilterWindowAction extends Action {

    public FilterWindowAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/filter_ps.gif"));
        this.setToolTipText("Show filter window");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
