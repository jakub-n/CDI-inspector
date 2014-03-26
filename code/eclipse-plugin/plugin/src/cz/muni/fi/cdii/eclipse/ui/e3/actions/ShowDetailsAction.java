package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.DetailsPart;


public class ShowDetailsAction extends Action {
    
    @Inject
    private IEclipseContext context; 

    public ShowDetailsAction() {
        super(null, IAction.AS_CHECK_BOX);
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/properties.gif"));
        this.setToolTipText("Show details window");
    }
    
    @Override
    public void run() {
        System.out.println("ahoj");
        System.out.println("context: " + this.context);
        MPart detailsMPart = this.getDetailsMPart();
        
        // TODO delete
//        Collection<MPart> parts = partService.getParts();
//        for (MPart part : parts) {
//            System.out.println("partId: " + part.toString());
//        }

        EPartService partService = context.get(EPartService.class);
        boolean isPartVisible = partService.isPartVisible(detailsMPart);
        if (isPartVisible) {
            partService.hidePart(detailsMPart);
        } else {
            partService.showPart(detailsMPart, PartState.ACTIVATE);
        }
    }
    
    private MPart getDetailsMPart() {
        EPartService partService = context.get(EPartService.class);
        MPart detailsMPart = partService.findPart(DetailsPart.PART_ID);
        if (detailsMPart == null) {
            detailsMPart = partService.createPart(DetailsPart.PART_ID);
        }
        return detailsMPart;
    }
}
