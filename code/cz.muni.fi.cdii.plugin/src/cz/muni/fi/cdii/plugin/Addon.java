package cz.muni.fi.cdii.plugin;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.IPartService;

public class Addon {
	
	@PostConstruct
	public void init(IEclipseContext context) {
		System.out.println("Addon.init()");
		CdiInspector inspector = new CdiInspector();
		context.set(CdiInspector.class, inspector);
		
		// activate message recieving in CdiInspector
		ContextInjectionFactory.inject(inspector, context);
	}

}
