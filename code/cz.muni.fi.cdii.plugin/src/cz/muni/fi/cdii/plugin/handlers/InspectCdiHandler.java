package cz.muni.fi.cdii.plugin.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.cdi.core.CDICoreNature;
import org.jboss.tools.cdi.core.CDIUtil;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;

import cz.muni.fi.cdii.plugin.utils.EclipseUtil;

public class InspectCdiHandler {
	
	@Execute
	public void execute (@Optional 
		    @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection, IEventBroker broker) {
		System.out.println("E4 cdi handler");
		System.out.println("selection: " + selection);
		if (!selection.isEmpty()) {
			EclipseUtil.showGraphByPackageExplorerSelection(selection, broker);
		}
	}

}
