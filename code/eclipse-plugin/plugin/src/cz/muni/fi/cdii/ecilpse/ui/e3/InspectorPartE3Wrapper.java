package cz.muni.fi.cdii.ecilpse.ui.e3;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.tools.compat.parts.DIViewPart;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;

import cz.muni.fi.cdii.eclipse.ui.e3.actions.CollapseAllAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ConnectToServerAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ExpandAllAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.RelayoutAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ReloadModelAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ResetZoom;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ShowDetailsAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ShowFilterAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.TmpAction1;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ZoomInAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ZoomOutAction;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;

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
	public static String VIEW_ID = "cz.muni.fi.cdii.eclipse.e3viewparts.cdii";

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
        toolBarManager.add(new ConnectToServerAction(this.getSite().getShell()));
        toolBarManager.add(new Separator());
        toolBarManager.add(this.injectInto(new ShowDetailsAction()));
        toolBarManager.add(this.injectInto(new ShowFilterAction()));
        toolBarManager.add(new Separator());
        toolBarManager.add(this.injectInto(new TmpAction1()));
    }
    
    private <T> T injectInto(T objectToInject) {
        ContextInjectionFactory.inject(objectToInject, this.getContext());
        return objectToInject;
    }

}
