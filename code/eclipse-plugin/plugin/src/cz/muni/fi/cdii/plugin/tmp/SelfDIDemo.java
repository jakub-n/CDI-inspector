package cz.muni.fi.cdii.plugin.tmp;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;

public class SelfDIDemo {
	
	@Inject
	private Logger logger;
	
	public SelfDIDemo(IEclipseContext context) {
		ContextInjectionFactory.inject(this, context);
	}
	
	public void log(String text) {
		logger.info("selfInjectedLog: " + text);
	}

}
