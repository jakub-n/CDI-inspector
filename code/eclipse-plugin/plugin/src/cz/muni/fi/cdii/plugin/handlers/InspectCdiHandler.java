package cz.muni.fi.cdii.plugin.handlers;

import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

import cz.muni.fi.cdii.plugin.inspection.InspectionJob;

@SuppressWarnings("restriction")
public class InspectCdiHandler {
	
	@Execute
	public void execute (
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection, 
			IEventBroker broker,
			Provider<StatusReporter> reporter) {
		System.out.println("E4 cdi handler");
		System.out.println("selection: " + selection);
		if (!selection.isEmpty()) {
			Job job = new InspectionJob("Inspection CDI beans", broker, selection);
			job.setPriority(Job.SHORT);
			job.schedule();
		}
	}

}
