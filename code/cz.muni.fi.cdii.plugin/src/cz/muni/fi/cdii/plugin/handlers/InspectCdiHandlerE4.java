package cz.muni.fi.cdii.plugin.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;

import cz.muni.fi.cdii.plugin.CdiInspector;
import cz.muni.fi.cdii.plugin.ICdiInspector;
import cz.muni.fi.cdii.plugin.utils.EclipseUtil;

public class InspectCdiHandlerE4 {
	
	@Execute
	public void execute (@Optional 
		    @Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection, ICdiInspector inspector) {
		System.out.println("E4 cdi handler");
		System.out.println("selection: " + selection);
		if (selection.isEmpty()) {
			EclipseUtil.showGraphByPackageExplorerSelection(selection);
		}
		// TODO delete
		inspector.inspect();
	}

}
