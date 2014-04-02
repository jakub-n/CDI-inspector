package cz.muni.fi.cdii.eclipse.inspection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;


public class LocalInspectionTask implements InspectionTask {
    
    private final IProject project;
    private final IEclipseContext context;
    
    public LocalInspectionTask(IProject project, IEclipseContext context) {
        this.project = project;
        this.context = context;
    }

    @Override
    public void run() {
        if (project.exists()) {
            Job inspectionJob = new LocalInspectionJob(this.project, this.context);
            inspectionJob.setPriority(Job.SHORT);
            inspectionJob.schedule();
        }
    }

}
