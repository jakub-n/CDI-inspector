package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;


public class ShowFilterAction extends Action {

    public ShowFilterAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/filter_ps.gif"));
        this.setToolTipText("Show filter window");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
    }
}
