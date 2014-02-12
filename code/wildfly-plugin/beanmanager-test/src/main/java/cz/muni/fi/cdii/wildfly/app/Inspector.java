package cz.muni.fi.cdii.wildfly.app;

import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Inspector {

	@Inject
	private BeanManager beanManager;
	
	public Set<Bean<?>> getBeans() {
		Set<Bean<?>> beans = this.beanManager.getBeans(Object.class,new AnnotationLiteral<Any>() {});
		return beans;
	}
}
