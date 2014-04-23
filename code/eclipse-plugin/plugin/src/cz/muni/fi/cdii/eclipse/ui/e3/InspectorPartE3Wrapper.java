package cz.muni.fi.cdii.eclipse.ui.e3;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.tools.compat.parts.DIViewPart;
import org.eclipse.jface.action.Action;
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
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ZoomInAction;
import cz.muni.fi.cdii.eclipse.ui.e3.actions.ZoomOutAction;
import cz.muni.fi.cdii.eclipse.ui.parts.InspectorPart;

/**
 * Encapsulation of standard e4 POJO part model class. It allows to utilize standard eclipse 3.x 
 * features like:
 * <ul>
 * <li>Show View dialog (shift-alt-q q)</>
 * <li>Outline view (unused)</li>
 * <li>Properties view (unused)</li>
 * </ul>
 *
 */
public class InspectorPartE3Wrapper extends DIViewPart<InspectorPart> {
	
	/**
	 * View ID as defined in {@code plugin.xml} descriptor
	 */
	public static String VIEW_ID = "cz.muni.fi.cdii.eclipse.e3viewparts.cdii";
	
	public Set<Action> enableableActions = new HashSet<>();

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
	    this.getComponent().setE3Wrapper(this);
	}

    private void addToolBarActions() {
        IToolBarManager toolBarManager = this.getViewSite().getActionBars().getToolBarManager();
        toolBarManager.add(addToEnableable(new ZoomInAction(this.getComponent())));
        toolBarManager.add(addToEnableable(new ZoomOutAction(this.getComponent())));
        toolBarManager.add(addToEnableable(new ResetZoom(this.getComponent())));
        toolBarManager.add(addToEnableable(new ExpandAllAction(this.getComponent())));
        toolBarManager.add(addToEnableable(new CollapseAllAction(this.getComponent())));
        toolBarManager.add(new Separator());
        toolBarManager.add(addToEnableable(new RelayoutAction(this.getComponent())));
        toolBarManager.add(addToEnableable(new ReloadModelAction(this.getComponent())));
        toolBarManager.add(this.injectInto(new ConnectToServerAction(this.getSite().getShell())));
        toolBarManager.add(new Separator());
        toolBarManager.add(this.injectInto(new ShowDetailsAction()));
        toolBarManager.add(this.injectInto(new ShowFilterAction()));
        updateToolbar();
    }
    
    public void updateToolbar() {
        for (Action action : this.enableableActions) {
            action.setEnabled(this.getComponent().isNonEmpty());
        }
    }
    
    private Action addToEnableable(Action action) {
        this.enableableActions.add(action);
        return action;
    }
    
    private <T> T injectInto(T objectToInject) {
        ContextInjectionFactory.inject(objectToInject, this.getContext());
        return objectToInject;
    }

}
