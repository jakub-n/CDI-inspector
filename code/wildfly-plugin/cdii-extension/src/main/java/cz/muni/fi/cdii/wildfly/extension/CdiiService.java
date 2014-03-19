package cz.muni.fi.cdii.wildfly.extension;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.weld.WeldBootstrapService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;


public class CdiiService implements Service<CdiiService> {

    private static final ServiceName SERVICE_NAME = ServiceName.of("cdii");
    
    private final Logger log = Logger.getLogger(CdiiService.class);
    private final ServiceName weldBootstrapServiceName;
    private volatile BeanManager beanManager = null;
    
    public CdiiService(ServiceName weldBootstrapServiceName) {
        this.weldBootstrapServiceName = weldBootstrapServiceName;
    }

    public static ServiceName deploymentServiceName(DeploymentUnit deploymentUnit) {
        return ServiceName.of(SERVICE_NAME, deploymentUnit.getName());
    }
    
    public BeanManager getBeanManager() {
        return beanManager;
    }

    @Override
    public CdiiService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public synchronized void start(StartContext context) throws StartException {
        ServiceController<?> serviceController =  context.getController().getServiceContainer()
                .getService(weldBootstrapServiceName);
        Object service = serviceController.getValue();
        if (service instanceof WeldBootstrapService) {
            final WeldBootstrapService weldBootstrapService = (WeldBootstrapService) service;
            this.beanManager = weldBootstrapService.getValue().getBeanManager();
        }
        if (this.beanManager == null) {
            log.warn("Obtaining BeanManager failed");
        }
    }

    @Override
    public synchronized void stop(StopContext context) {
        this.beanManager = null;
    }

}
