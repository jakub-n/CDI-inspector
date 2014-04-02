package cz.muni.fi.cdii.eclipse.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;

import cz.muni.fi.cdii.eclipse.inspection.LocalInspectionTask;

public class InspectCdiHandler {
    
    @Inject
    private Logger log;
	
	@Execute
	public void execute (
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection,
			IEclipseContext context) {
		if (!selection.isEmpty()) {
		    IProject project = this.getProjectFromPackageExplorerSelection(selection);
		    LocalInspectionTask task = new LocalInspectionTask(project, context);
		    task.run();
		}
	}
	
    private IProject getProjectFromPackageExplorerSelection(
            final IStructuredSelection selection) {
        Object selectedElement = selection.getFirstElement();
        if (selectedElement instanceof IJavaProject) {
            final IJavaProject javaProject = (IJavaProject) selectedElement;
            return javaProject.getProject();
        }
        this.log.info("Selection can't be converted to JavaProject");
        return null;
    }

}
