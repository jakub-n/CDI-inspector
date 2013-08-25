package cz.muni.fi.cdii.plugin;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.Event;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;
import cz.muni.fi.cdii.plugin.ui.e3compat.InspectorPartE3Wrapper;

/**
 * Entry point for showing the the inspection graph
 *
 */
@SuppressWarnings("restriction")
public class CdiInspector implements ICdiInspector {
	
	@Inject
	private Logger log;
	
	/**
	 * It shows inspection part and updates dependency graph based on {@value inspection}
	 * @param inspection dependency graph description
	 */
	@Inject
	@Optional
	public void inspect(@UIEventTopic(CdiInspection.INSPECT_TOPIC) CdiInspection inspection) {
		this.log.info("CdiInspector.inspect()");
	
		/* <e3specific> */
		IViewPart viewPart = openViewPart();
		passInspection(inspection, viewPart);
		/* </e3specific> */
		
	}

	private void passInspection(CdiInspection inspection, IViewPart viewPart) {
		if (viewPart instanceof InspectorPartE3Wrapper) {
			final InspectorPartE3Wrapper inspectorViewPart = (InspectorPartE3Wrapper) viewPart;
			inspectorViewPart.getComponent().inspect(inspection);
		} else {
			log.warn("IViewPart instance of type " 
					+ InspectorPartE3Wrapper.class.getCanonicalName() + "expected; "
					+ viewPart.getClass().getCanonicalName() + " obtained.");
		}
	}

	/**
	 * @return opened {@link IViewPart} instance or null
	 */
	private IViewPart openViewPart() {
		try {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView(InspectorPartE3Wrapper.VIEW_ID);
		} catch (PartInitException e) {
			log.warn(e, "Inspector part failed to open.");
		}
		return null;
	}

	@Override
	public void handleEvent(Event event) {
		final CdiInspection inspection = (CdiInspection) event.getProperty(IEventBroker.DATA);
		this.inspect(inspection);
	}
}
