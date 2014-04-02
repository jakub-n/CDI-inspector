package cz.muni.fi.cdii.eclipse.inspection;

import cz.muni.fi.cdii.common.model.Model;

public class Inspection {

    private final Model model;
    
    private final InspectionTask task;
    
    public Inspection(Model model, InspectionTask task) {
        super();
        this.model = model;
        this.task = task;
    }

    public Model getModel() {
        return model;
    }
    
    public InspectionTask getTask() {
        return task;
    }
    
    
}
