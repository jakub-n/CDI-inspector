package cz.muni.fi.cdii.plugin;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;


// TODO delete
public class RegisterBroadcastReceiverProcessor {
	
	@Inject
	Logger log;
	

	@Execute
	public void execute(IEclipseContext context) {
		System.out.println("processor");
		this.log.info("debug: processor");
		
		CdiInspector inspector = new CdiInspector();
		context.set(CdiInspector.class, inspector);
		
		// activate message recieving in CdiInspector
		ContextInjectionFactory.inject(inspector, context);
	}
}
