package cz.muni.fi.cdii.eclipse.inspection;

import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

public class RemoteInspectionJob extends Job {

    private URL cdiiRestUrl;

    public RemoteInspectionJob(URL cdiiRestUrl) {
        super("Inspecting remote CDI app: " + cdiiRestUrl.toString());
        this.cdiiRestUrl = cdiiRestUrl;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        return null;
    }

}
