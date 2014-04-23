package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import java.net.URL;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.inspection.RemoteInspectionTask;
import cz.muni.fi.cdii.eclipse.ui.connectdialog.ConnectToServerDialog;


public class ConnectToServerAction extends Action {
    
    final private Shell shell;
    
    @Inject
    IEclipseContext context;
    
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
            URL selectedUrl = dialog.getSelectedUrl();
            RemoteInspectionTask remoteInspectionTask = new RemoteInspectionTask(selectedUrl, 
                    this.context);
            remoteInspectionTask.run();
        }
    }
}
