 
package cz.muni.fi.cdii.plugin.tmp;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import cz.muni.fi.cdii.plugin.ui.InspectorPart;

public class OnPartMenuItem {
	@Execute
	public void execute(EPartService partService) {
		System.out.println("on part menu item");
		MPart inspectorPart = partService.findPart(InspectorPart.ID);
		MToolBar toolbar = inspectorPart.getToolbar();
		System.out.println("toolbar: " + toolbar);
	}
		
}