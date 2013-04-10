package cz.muni.fi.cdii.plugin.utils;

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
import org.jboss.tools.cdi.internal.core.impl.CDIProject;

import cz.muni.fi.cdii.plugin.LocalInspection;
import cz.muni.fi.cdii.plugin.model.IInspection;

public class EclipseUtil {
	
	public static void showGraphByPackageExplorerSelection(final IStructuredSelection selection, 
			final IEventBroker broker) {
		assert !selection.isEmpty();
		
		System.out.println("showGraphByPackageExplorerSelection method executed, broker:" + broker);
		
		Job job = new Job("Inspectiong CDI beans") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO delete 
				System.out.println("job executed");
				Object selectedElement = selection.getFirstElement();
				IProject project = EclipseUtil.getProjectFromPackageExplorerElement(selectedElement);
				ICDIProject cdiProject = EclipseUtil.getCdiProjectFromProject(project);
				LocalInspection inspection = new LocalInspection(cdiProject);
				broker.post(IInspection.INSPECT_TOPIC, inspection);
				System.out.println("showGraphByPackageExplorerSelection inspect event posted");
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();
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
	
	private static ICDIProject getCdiProjectFromProject(IProject project) {
		CDICoreNature nature = CDIUtil.getCDINatureWithProgress(project);
		ICDIProject cdiProject = nature.getDelegate();
		return cdiProject;
	}
	
}
