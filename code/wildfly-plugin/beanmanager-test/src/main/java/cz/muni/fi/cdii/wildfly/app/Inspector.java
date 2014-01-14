package cz.muni.fi.cdii.wildfly.app;

import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Named
public class Inspector {

	@Inject
	private BeanManager beanManager;
	
//	@Resource(mappedName="java:comp/BeanManager")
//	private BeanManager beanManager2;
	
	public Set<Bean<?>> getBeans() {
//		BeanManager beanManager3;
//		try {
//			beanManager3 = (BeanManager) InitialContext.doLookup("java:comp/BeanManager");
//		} catch (NamingException ex) {
//			throw new RuntimeException(ex);
//		}
//
//		System.out.println("beanmanager: " + beanManager);
//		System.out.println("beanmanager2: " + beanManager2);
//		System.out.println("beanmanager3: " + beanManager3);
		Set<Bean<?>> beans = this.beanManager.getBeans(Object.class,new AnnotationLiteral<Any>() {});
		return beans;
	}
}
