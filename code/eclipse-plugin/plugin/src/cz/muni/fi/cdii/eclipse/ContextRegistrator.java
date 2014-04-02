package cz.muni.fi.cdii.eclipse;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import cz.muni.fi.cdii.eclipse.ui.e3.actions.TmpAction1;
import cz.muni.fi.cdii.plugin.common.model.CdiInspection;


public class ContextRegistrator {
	
	/**
	 * This method is executed at startup to create and register {@link InspectionSubscriber} instance for
	 * listening for {@link CdiInspection#INSPECT_TOPIC} e4 events. 
	 * @param context
	 */
	@Execute
	public void execute(IEclipseContext context, IEventBroker broker) {
	    // TODO delete
		System.out.println("ContextRegistrator.execute()");
		final InspectionSubscriber inspectionSubscriber = ContextInjectionFactory.make(InspectionSubscriber.class, context);
		context.set(InspectionSubscriber.class, inspectionSubscriber);
		// TODO remove after https://bugs.eclipse.org/bugs/show_bug.cgi?id=412554 resolution
		broker.subscribe(CdiiEventTopics.INSPECT, inspectionSubscriber);
		
		// TODO delete
		TmpSubscriber tmpSubscriber = ContextInjectionFactory.make(TmpSubscriber.class, context);
		context.set(TmpSubscriber.class, tmpSubscriber);
		broker.subscribe(TmpAction1.EVENT, tmpSubscriber);
	}

}
