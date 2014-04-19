package cz.muni.fi.cdii.eclipse.inspection;

import java.net.URL;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;

public class RemoteInspectionTask implements InspectionTask {

    private final URL cdiiRestUrl;
    private IEclipseContext context;
    
    public RemoteInspectionTask(URL cdiiRestUrl, IEclipseContext context) {
        this.cdiiRestUrl = cdiiRestUrl;
        this.context = context;
    }

    @Override
    public void run() {
        Job inspectionJob = new RemoteInspectionJob(this.cdiiRestUrl, this.context);
        inspectionJob.setPriority(Job.SHORT);
        inspectionJob.schedule();
    }
    
}
