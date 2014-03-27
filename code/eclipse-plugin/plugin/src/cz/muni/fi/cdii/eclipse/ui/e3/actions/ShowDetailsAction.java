package cz.muni.fi.cdii.eclipse.ui.e3.actions;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.action.Action;

import cz.muni.fi.cdii.eclipse.Activator;
import cz.muni.fi.cdii.eclipse.ui.parts.DetailsPart;


public class ShowDetailsAction extends Action {
    
    @Inject
    private IEclipseContext context; 

    public ShowDetailsAction() {
        super();
        this.setImageDescriptor(Activator.getImageDescriptor("icons/eclipse/properties.gif"));
        this.setToolTipText("Show details window");
    }
    
    @Override
    public void run() {
//        MPart detailsMPart = this.getDetailsMPart();
//        System.out.println("on top " + detailsMPart.isOnTop() + ", to be rendered " + detailsMPart.isToBeRendered() + ", visible " + detailsMPart.isVisible());


        EPartService partService = context.get(EPartService.class);
        partService.showPart(DetailsPart.PART_ID, PartState.ACTIVATE);
//        boolean isToBeRendered = detailsMPart.isToBeRendered();
//        if (isToBeRendered) {
//            partService.hidePart(detailsMPart);
//        } else {
//            partService.showPart(detailsMPart, PartState.ACTIVATE);
//        }
    }
    
//    private MPart getDetailsMPart() {
//        EPartService partService = context.get(EPartService.class);
//        MPart detailsMPart = partService.findPart(DetailsPart.PART_ID);
//        if (detailsMPart == null) {
//            detailsMPart = partService.createPart(DetailsPart.PART_ID);
//        }
//        return detailsMPart;
//    }
}
