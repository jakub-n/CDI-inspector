package cz.muni.fi.cdii.eclipse.inspection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.eclipse.Activator;

public class RemoteInspectionJob extends Job {

    private static final String CDII_URL_SUFFIX = "cdii";
    
    /**
     * base url; cdii suffix needs to be added
     */
    private URL inspectionUrl;
    
    @Inject
    private IEclipseContext context;
    
    @Inject
    private IEventBroker broker;

    public RemoteInspectionJob(URL baseUrl, IEclipseContext context) {
        super("Inspecting remote CDI app: " + baseUrl.toString());
        this.inspectionUrl = getInspectionUrl(baseUrl);
        ContextInjectionFactory.inject(this, context);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Model model;
        try {
            model = mapper.readValue(this.inspectionUrl, Model.class);
        } catch (IOException e) {
            return handleMapperError(e);
        }
        InspectionTask task = new RemoteInspectionTask(this.inspectionUrl, this.context);
        IStatus status = Utils.createGraphInspectionAndDispatch(model, task, this.broker);
        return status;
    }
    
    private static URL getInspectionUrl(URL baseUrl) {
        String baseUrlString = baseUrl.toString();
        if (!baseUrlString.endsWith("/")) {
            baseUrlString += "/";
        }
        String inspectionUrlString = baseUrlString + CDII_URL_SUFFIX;
        try {
            return new URL(inspectionUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Server URL for CDI inspection has invalid format.", e);
        }
    }

    private Status handleMapperError(IOException e) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                MessageBox messageBox = new MessageBox(
                        Display.getDefault().getActiveShell(), SWT.ICON_WARNING | SWT.OK);
                messageBox.setText("CDI Inspector");
                messageBox.setMessage("Remote CDI inspection failed."
                        + "\n"
                        + "Inspection URL: " + RemoteInspectionJob.this.inspectionUrl);
                messageBox.open();
            }
        });
        return new Status(Status.WARNING, Activator.PLUGIN_ID, 
                "Remote CDI inspection of application " + this.inspectionUrl.toString() 
                + " failed.", e);
    }

}
