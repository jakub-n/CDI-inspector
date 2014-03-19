package cz.muni.fi.cdii.wildfly.deployment;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.weld.WeldBootstrapService;
import org.jboss.as.weld.util.Utils;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.wildfly.extension.undertow.deployment.UndertowAttachments;

import cz.muni.fi.cdii.wildfly.extension.CdiiService;

public class CdiiDeploymentProcessor implements DeploymentUnitProcessor {

    final Logger log = Logger.getLogger(CdiiDeploymentProcessor.class);

    public static final Phase PHASE = Phase.POST_MODULE;
    public static final int PRIORITY = Phase.POST_MODULE_UNDERTOW_HANDLERS;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext)
            throws DeploymentUnitProcessingException {
        final CdiiService cdiiService = createAndInstallCdiiService(phaseContext);
        phaseContext.getDeploymentUnit().addToAttachmentList(
                UndertowAttachments.UNDERTOW_OUTER_HANDLER_CHAIN_WRAPPERS,
                getCdiiHandler(cdiiService));
        log.info("\"" + phaseContext.getDeploymentUnit().getName() + "\" cdii attached");
    }

    private CdiiService createAndInstallCdiiService(DeploymentPhaseContext phaseContext) {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
        final ServiceName cdiiServiceName = CdiiService.deploymentServiceName(deploymentUnit);
        // org.jboss.as.weld.deployment.processors.WeldDeploymentProcessor line 120
        final ServiceName weldBootstrapServiceName = Utils.getRootDeploymentUnit(deploymentUnit)
                .getServiceName().append(WeldBootstrapService.SERVICE_NAME);
        final CdiiService cdiiService = new CdiiService(weldBootstrapServiceName);
        final ServiceBuilder<CdiiService> cdiiServiceBuilder = serviceTarget.addService(
                cdiiServiceName, cdiiService);
        cdiiServiceBuilder.addDependency(weldBootstrapServiceName);
        cdiiServiceBuilder.install();
        return cdiiService;
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    private static HandlerWrapper getCdiiHandler(final CdiiService cdiiService) {
        return new HandlerWrapper() {

            @Override
            public HttpHandler wrap(final HttpHandler handler) {
                return new CdiiHttpHandler(cdiiService.getBeanManager(), handler);
            }
        };
    }

}
