package cz.muni.fi.cdii.webapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class MyScopeExtension implements Extension {

	public void afterDiscovery(@Observes AfterBeanDiscovery event, BeanManager manager) {
		System.out.println("after discovery");
		Context requestContext = manager.getContext(ApplicationScoped.class);
		event.addContext(new MyScopeContext(requestContext));
	}
}
