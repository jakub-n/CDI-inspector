package cz.muni.fi.cdii.plugin.tmp;

import javax.inject.Inject;

import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class StandardBean {
	
	@Inject
	EPartService partService;
	
	public void openPart() {
		partService.showPart(Part0.ID, PartState.ACTIVATE);
	}

}
