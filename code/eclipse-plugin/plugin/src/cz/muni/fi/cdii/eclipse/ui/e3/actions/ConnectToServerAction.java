package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.connectdialog.ConnectToServerDialog;


public class ConnectToServerAction extends Action {
    
    final private Shell shell;
    
    public ConnectToServerAction(Shell shell) {
        super();
        this.shell = shell;
        this.setImageDescriptor(
                Activator.getImageDescriptor("icons/eclipse/server_perspective.gif"));
        this.setToolTipText("Inspect running application");
    }
    
    @Override
    public void run() {
        ConnectToServerDialog dialog = new ConnectToServerDialog(this.shell);
        int dialogResult = dialog.open();
        if (dialogResult == Dialog.OK) {
            String selectedUrl = dialog.getSelectedUrl().toString();
            System.out.println("selected URL: " + selectedUrl);
        }
    }
}
