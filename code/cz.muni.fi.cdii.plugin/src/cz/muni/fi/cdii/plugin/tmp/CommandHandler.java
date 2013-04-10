package cz.muni.fi.cdii.plugin.tmp;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import cz.muni.fi.cdii.plugin.Activator;
import cz.muni.fi.cdii.plugin.model.IInspection;

public class CommandHandler {
	
	@Inject
	private EPartService injectedPartService;
	
	@Inject
	private IEclipseContext injectedEclipseContext;
	
	@Execute
	public void execute(IEventBroker broker) {
		System.out.println("Command handler");
		//this.injectedPartService.showPart(Part0.ID, PartState.ACTIVATE);
		//(new StandardBean()).openPart();
		System.out.println("ahoj svete handler");
		IEclipseContext eclipseContext = E4Workbench.getServiceContext();
		EPartService partService = eclipseContext.get(EPartService.class);
		//partService.showPart(Part0.ID, PartState.ACTIVATE);
		//SelfDIDemo diDemo = new SelfDIDemo(injectedEclipseContext);
		//diDemo.log("hello world");
		//broker.post(IInspection.INSPECT_TOPIC, "Hello World event");
	}
}
