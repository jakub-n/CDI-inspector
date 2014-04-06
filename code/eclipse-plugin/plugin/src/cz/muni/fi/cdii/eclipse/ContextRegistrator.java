package cz.muni.fi.cdii.eclipse;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;


public class ContextRegistrator {
	
	/**
	 * This method is executed at startup to create and register {@link InspectionSubscriber} 
	 * instance for
	 * listening for {@link CdiInspection#INSPECT_TOPIC} e4 events. 
	 * @param context
	 */
	@Execute
	public void execute(IEclipseContext context, IEventBroker broker) {
		final InspectionSubscriber inspectionSubscriber = ContextInjectionFactory
		        .make(InspectionSubscriber.class, context);
		context.set(InspectionSubscriber.class, inspectionSubscriber);
		// remove after https://bugs.eclipse.org/bugs/show_bug.cgi?id=412554 resolution
		broker.subscribe(CdiiEventTopics.INSPECT, inspectionSubscriber);
	}

}
