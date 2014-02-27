package cz.muni.fi.cdii.plugin.inspection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.ICDIProject;

import cz.muni.fi.cdii.plugin.Activator;
import cz.muni.fi.cdii.plugin.common.model.CdiInspection;
import cz.muni.fi.cdii.plugin.inspection2.Inspection;
import cz.muni.fi.cdii.plugin.visual.LocalInspectionException;
import cz.muni.fi.cdii.plugin.visual.model.LocalCdiInspection;
import cz.muni.fi.cdii.plugin.visual.model.LocalInspectionFactory;

/**
 * Eclipse fashion thread encapsulation of local project cdi metadata extraction.
 *
 */
public class InspectionJob extends Job {
	
	private final IEventBroker broker;
	private final IStructuredSelection selection;

	public InspectionJob(String name, IEventBroker broker,
			IStructuredSelection selection) {
		super(name);
		this.broker = broker;
		this.selection = selection;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			return checkedRun();
		} catch (LocalInspectionException ex) {
			return new Status(Status.ERROR, Activator.PLUGIN_ID, "Local inspection failed", 
					ex);
		}
	}

	public IStatus checkedRun() {
		Object selectedElement = selection.getFirstElement();
		IProject project = InspectionJob.getProjectFromPackageExplorerElement(selectedElement);
		ICDIProject cdiProject = InspectionJob.getCdiProjectFromProject(project);
		LocalCdiInspection inspection = LocalInspectionFactory.createInspection(cdiProject);
		new Inspection(cdiProject);
		broker.post(CdiInspection.INSPECT_TOPIC, inspection);
		System.out.println("showGraphByPackageExplorerSelection inspect event posted");
		return Status.OK_STATUS;
	}

	private static ICDIProject getCdiProjectFromProject(IProject project) {
		CDICoreNature nature = CDIUtil.getCDINatureWithProgress(project);
		ICDIProject cdiProject = nature.getDelegate();
		return cdiProject;
	}

	private static IProject getProjectFromPackageExplorerElement(final Object selectedElement) 
		throws IllegalArgumentException {
		if (selectedElement instanceof IJavaProject) {
			final IJavaProject javaProject = (IJavaProject) selectedElement;
			return javaProject.getProject();
		}
		if (selectedElement instanceof IPackageFragment
				|| selectedElement instanceof ICompilationUnit) {
			final IJavaElement javaElement = (IJavaElement) selectedElement;
			return  javaElement.getJavaProject().getProject();
		}
		throw new IllegalArgumentException("Unexpected type of selected element: " 
				+ selectedElement.getClass().getName());
	}
}