package cz.muni.fi.cdii.wildfly.deployment;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.weld.WeldBootstrapService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.wildfly.extension.undertow.deployment.UndertowAttachments;

public class CdiiDeploymentProcessor implements DeploymentUnitProcessor {

    final Logger log = Logger.getLogger(CdiiDeploymentProcessor.class);

    public static final Phase PHASE = Phase.POST_MODULE;

    public static final int PRIORITY = Phase.POST_MODULE_UNDERTOW_HANDLERS;
//    public static final int PRIORITY = 0x4000;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        phaseContext.getDeploymentUnit().addToAttachmentList(
                UndertowAttachments.UNDERTOW_OUTER_HANDLER_CHAIN_WRAPPERS, 
                getCdiiHandler(phaseContext.getServiceRegistry()));
        log.info("\"" + phaseContext.getDeploymentUnit().getName() + "\" cdii attached");
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }
    
    private static HandlerWrapper getCdiiHandler(final ServiceRegistry serviceRegistry) {
        final BeanManager beanManager = getBeanManager(serviceRegistry);
        return new HandlerWrapper() {
            
            @Override
            public HttpHandler wrap(final HttpHandler handler) {
                return new CdiiHttpHandler(beanManager, handler);
            }
        };
    }
    
    private static BeanManager getBeanManager(final ServiceRegistry serviceRegistry) {
        final ServiceName serviceName = WeldBootstrapService.SERVICE_NAME;
        ServiceController<?> container = serviceRegistry.getService(serviceName);
        if (container != null) {
            final WeldBootstrapService weldService = (WeldBootstrapService) container.getValue();
            final BeanManager beanManager = weldService.getBeanManager();
            return beanManager;
        }
        return null;
    }
    
}
