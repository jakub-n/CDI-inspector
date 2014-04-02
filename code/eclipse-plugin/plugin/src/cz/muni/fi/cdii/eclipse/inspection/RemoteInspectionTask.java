package cz.muni.fi.cdii.eclipse.inspection;

import java.net.URL;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class RemoteInspectionTask implements InspectionTask {

    private final URL cdiiRestUrl;
    
    public RemoteInspectionTask(URL cdiiRestUrl) {
        this.cdiiRestUrl = cdiiRestUrl;
    }

    @Override
    public void run() {
        Job inspectionJob = new RemoteInspectionJob(this.cdiiRestUrl);
        inspectionJob.setPriority(Job.SHORT);
        inspectionJob.schedule();
    }
    
}
