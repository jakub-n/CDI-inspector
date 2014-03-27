package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.FilterPart;


public class ShowFilterAction extends Action {

    @Inject
    private IEclipseContext context; 

    public ShowFilterAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/filter_ps.gif"));
        this.setToolTipText("Show filter window");
    }
    
    @Override
    public void run() {
        EPartService partService = context.get(EPartService.class);
        partService.showPart(FilterPart.PART_ID, PartState.ACTIVATE);
    }
}
