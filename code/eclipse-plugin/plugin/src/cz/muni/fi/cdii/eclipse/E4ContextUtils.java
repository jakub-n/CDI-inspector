package cz.muni.fi.cdii.eclipse;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.framework.BundleContext;

public class E4ContextUtils {

    /**
     * Method provide injection from top most OSGI context
     * <br>
     * i.e. such context does not contain e.g. Logger, {@link IEventBroker} depends on logger
     * @param injectionTarget
     */
    public static void inject(Object injectionTarget) {
        IEclipseContext context = getEclipseContext();
        ContextInjectionFactory.inject(injectionTarget, context);
    }
    
    private static IEclipseContext getEclipseContext() {
        BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
        IEclipseContext eclipseContext = EclipseContextFactory.getServiceContext(bundleContext);
        return eclipseContext;
    }
}
