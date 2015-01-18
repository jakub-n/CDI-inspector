package cz.muni.fi.cdii.eclipse.inspection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;

import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.eclipse.CdiiEventTopics;

public class Utils {

    private Utils() {}
    
    /**
     * Common last step of both local and remote inspections. It creates graph model and pass in
     * to inspection part using {@link CdiiEventTopics#INSPECT} event.
     * @param model collected data
     * @param task to repeat current inspection
     * @param broker e4 event broker
     * @return {@link Status#OK_STATUS}
     */
    public static IStatus createGraphInspectionAndDispatch(Model model, InspectionTask task, 
            IEventBroker broker) {
        GraphInspection graphInspection = new GraphInspection(model, task);
        broker.post(CdiiEventTopics.INSPECT, graphInspection);
        return Status.OK_STATUS;
    }
    
    public static void runJob(Job job) {
    	job.setPriority(Job.SHORT);
    	job.schedule();
    }
}
