package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.e4.tools.compat.parts.DIViewPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;

import cz.muni.fi.cdii.plugin.Activator;
import cz.muni.fi.cdii.plugin.ui.InspectorPart;

/**
 * Encapsulation of standard e4 POJO part model class. It allows to utilize standard eclipse 3.x 
 * features like:
 * <ul>
 * <li>Show View dialog (shift-alt-q q)</>
 * <li>Outline view</li>
 * <li>Properties view</li>
 * </ul>
 *
 */
public class InspectorPartE3Wrapper extends DIViewPart<InspectorPart> {
	
	/**
	 * View ID as defined in {@code plugin.xml} descriptor
	 */
	public static String VIEW_ID = "cz.muni.fi.cdii.plugin.cdiInspectorView";

	public InspectorPartE3Wrapper() {
		super(InspectorPart.class);
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return super.getAdapter(adapter);
	}
	
	@Override
	public void createPartControl(Composite parent) {
	    super.createPartControl(parent);
	    this.addToolBarActions();
	}

    private void addToolBarActions() {
        IToolBarManager toolBarManager = this.getViewSite().getActionBars().getToolBarManager();
        toolBarManager.add(new ZoomInAction());
        toolBarManager.add(new ZoomOutAction());
        toolBarManager.add(new ResetZoom());
        toolBarManager.add(new ExpandAllAction());
        toolBarManager.add(new CollapseAllAction());
        toolBarManager.add(new Separator());
        toolBarManager.add(new RelayoutAction());
        toolBarManager.add(new ReloadModelAction());
        toolBarManager.add(new ConnectToServerAction());
        toolBarManager.add(new Separator());
        toolBarManager.add(new DetailsWindowAction());
        toolBarManager.add(new FilterWindowAction());
        toolBarManager.add(new Separator());
        toolBarManager.add(new TmpAction1());
    }

}
