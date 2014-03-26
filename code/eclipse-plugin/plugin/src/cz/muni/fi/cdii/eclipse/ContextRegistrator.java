package cz.muni.fi.cdii.eclipse;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;


public class ContextRegistrator {
	
	/**
	 * This method is executed at startup to create and register {@link CdiInspector} instance for
	 * listening for {@link CdiInspection#INSPECT_TOPIC} e4 events. 
	 * @param context
	 */
	@Execute
	public void execute(IEclipseContext context, IEventBroker broker) {
		System.out.println("ContextRegistrator.execute()");
		final CdiInspector inspector = ContextInjectionFactory.make(CdiInspector.class, context);
		context.set(ICdiInspector.class, inspector);
		// TODO remove after https://bugs.eclipse.org/bugs/show_bug.cgi?id=412554 resolution
		broker.subscribe(CdiInspection.INSPECT_TOPIC, inspector);
	}

}
