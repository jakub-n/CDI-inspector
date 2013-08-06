package cz.muni.fi.cdii.plugin.visual.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;

import cz.muni.fi.cdii.plugin.common.model.Scope;

public class LocalInspectionFactory {
	
	private Set<LocalClass> classes = new HashSet<LocalClass>();
	
	private LocalInspectionFactory() {
	}
	
	public static LocalCdiInspection createInspection(final ICDIProject cdiProject) {
		LocalInspectionFactory factory = new LocalInspectionFactory();
		LocalCdiInspection result = factory.inspect(cdiProject);
		return result;
	}

	private LocalCdiInspection inspect(ICDIProject cdiProject) {

		/*
		 * all possible instances (14):
		 * BuiltInBean
		 * CDIBean
		 * ClassBean
		 * ConversationBuiltInBean
		 * DecoratorBean
		 * EventBean
		 * GenericBeanProducerField
		 * GenericBeanProducerMethod
		 * GenericClassBean
		 * InterceptorBean
		 * NewBean
		 * ProducerField
		 * ProducerMethod
		 * SessionBean
		 */
		for (IBean bean : cdiProject.getBeans()) {
			System.out.println(bean.getClass().getName());
			if (bean instanceof IClassBean) {
				final IClassBean iClassBean = (IClassBean) bean;
				processIClassBean(iClassBean);
			}
		}
		LocalCdiInspection inspection = new LocalCdiInspection();
		inspection.setClasses(this.classes);
		return inspection;
	}

	/**
	 * Covers (9):
	 * BuiltInBean
	 * CDIBean
	 * ClassBean
	 * ConversationBuiltInBean
	 * DecoratorBean
	 * GenericClassBean
	 * InterceptorBean
	 * NewBean
	 * SessionBean
	 */
	private void processIClassBean(IClassBean bean) {
		LocalClass localClass = new LocalClass();
		
		// class part
		String qualifiedName = this.getFullyQualifiedName(bean.getBeanClass());
		int lastDotIndex = qualifiedName.lastIndexOf(".");
		String packageName = qualifiedName.substring(0, lastDotIndex);
		String className = qualifiedName.substring(lastDotIndex + 1, qualifiedName.length());
		localClass.setPackageName(packageName);
		localClass.setClassName(className);
		localClass.setNote(bean.getClass().getSimpleName());
		this.classes.add(localClass);
		
		// bean part
		Scope scope = new Scope(bean.getScope().getElementName(), 
				bean.getScope() == null ? false : bean.getScope().isNorlmalScope());
		LocalCdiProperties properties = new LocalCdiProperties();
		properties.setScope(scope);
		LocalBean localBean = new LocalBean(localClass);
		localBean.setProperties(properties);
		
	}

	private String getFullyQualifiedName(IType type) {
		String qualifiedName;
		try {
			qualifiedName = type.getFullyQualifiedParameterizedName();
		} catch (JavaModelException e) {
			qualifiedName = type.getFullyQualifiedName();
		}
		return qualifiedName;
	}
}
