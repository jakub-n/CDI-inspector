package cz.muni.fi.cdii.plugin;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.ui.IPartService;

import cz.muni.fi.cdii.plugin.model.IInspection;
import cz.muni.fi.cdii.plugin.ui.InspectorPart;


public class CdiInspector {
	
	@Inject
	private Logger log;
	
	@Inject
	private EPartService partService;
	
	
	public CdiInspector() {
		System.out.println("CdiInspector.init()");
	}
	
	@Inject
	@Optional
	public void inspect(@UIEventTopic(IInspection.INSPECT_TOPIC) IInspection inspection) {
		System.out.println("CdiInspector.inspect()");
		this.partService.showPart(InspectorPart.ID, PartState.ACTIVATE);
		MPart inspectorMPart = this.partService.findPart(InspectorPart.ID);
		Object partObject = inspectorMPart.getObject();
		if (partObject instanceof InspectorPart) {
			final InspectorPart inspectorPart = (InspectorPart) partObject;
			inspectorPart.inspect(inspection);
		} else {
			throw new RuntimeException("Inspector part contains object of unexpected type: " 
					+ partObject.getClass().getName());
		}
		
	}
}
