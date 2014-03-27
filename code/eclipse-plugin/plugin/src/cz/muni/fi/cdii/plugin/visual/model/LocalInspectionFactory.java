package cz.muni.fi.cdii.plugin.visual.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IQualifierDeclaration;
import org.jboss.tools.cdi.core.IScope;
import org.jboss.tools.cdi.core.IScopeDeclaration;
import org.jboss.tools.cdi.core.IStereotypeDeclaration;
import org.jboss.tools.common.java.IAnnotationDeclaration;

import cz.muni.fi.cdii.plugin.common.model.Scope;

public class LocalInspectionFactory {
	
	private Set<LocalClass> classes = new HashSet<>();
	private Map<IClassBean, LocalBean> beanMap = new HashMap<>();
	
	private LocalInspectionFactory() {
	}
	
	/**
	 * Method creates internal local CDI project object model descriptor.
	 *
	 */
	public static LocalCdiInspection createInspection(final ICDIProject cdiProject) {
		LocalInspectionFactory factory = new LocalInspectionFactory();
		LocalCdiInspection result = factory.inspect(cdiProject);
		return result;
	}

	private LocalCdiInspection inspect(ICDIProject cdiProject) {

		/*
		 * all possible instances (14):
		 * BuiltInBean *
		 * CDIBean *
		 * ClassBean *
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
//			System.out.println(bean.getClass().getName());
			if (bean instanceof IClassBean) {
				final IClassBean iClassBean = (IClassBean) bean;
				processIClassBean(iClassBean);
			}
//			if (bean instanceof ProducerMethod) {
//				ProducerMethod method = (ProducerMethod) bean;
//				for (IQualifierDeclaration qualifierDeclaration : bean.getQualifierDeclarations()) {
//					qualifierDeclaration.getMemberValuePairs();
//				}
//			}
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
		LocalClass localClass = addClass(bean);
		addBean(bean, localClass);
	}

	private LocalClass addClass(IClassBean bean) {
		LocalClass localClass = new LocalClass();
		String qualifiedName = this.getFullyQualifiedName(bean.getBeanClass());
		int lastDotIndex = qualifiedName.lastIndexOf(".");
		String packageName = qualifiedName.substring(0, lastDotIndex);
		String className = qualifiedName.substring(lastDotIndex + 1, qualifiedName.length());
		localClass.setPackageName(packageName);
		localClass.setClassName(className);
		localClass.setNote(bean.getClass().getSimpleName());
		this.classes.add(localClass);
		return localClass;
	}

	private void addBean(IClassBean bean, LocalClass localClass) {
		LocalBean localBean = new LocalBean(localClass);
		LocalCdiProperties properties = extractCdiProperties(bean);
		localBean.setProperties(properties);
		this.beanMap.put(bean, localBean);
	}
	
	// TODO finish
	private static LocalCdiProperties extractCdiProperties(IClassBean bean) {
		LocalCdiProperties result = new LocalCdiProperties();
		Scope scope = extractScope(bean);
		result.setScope(scope);
		result.setName(bean.getName());
		Set<String> qualifiers = extractQualifiers(bean);
		result.setQualifiers(qualifiers);
		Set<String> stereotypes = extractStereotypes(bean);
		result.setStereotypes(stereotypes);
		return result;
	}

	private static Set<String> extractQualifiers(IClassBean bean) {
		Set<String> result = new HashSet<>();
		for (IQualifierDeclaration declaration : bean.getQualifierDeclarations()) {
			String qualifierName = extractAnnotation(declaration);
			result.add(qualifierName);
		}
		return result;
	}
	
	private static String extractAnnotation(IAnnotationDeclaration declaration) {
		StringBuilder qualifierLineBuilder = new StringBuilder();
		qualifierLineBuilder.append("@")
				.append(declaration.getTypeName());
		if (declaration.getMemberValuePairs().length > 0) {
			qualifierLineBuilder.append("(");
			for (IMemberValuePair pair : declaration.getMemberValuePairs()) {
				qualifierLineBuilder.append(pair.getMemberName())
					.append(" = ")
					.append(pair.getValue())
					.append(", ");
			}
			qualifierLineBuilder.delete(qualifierLineBuilder.length()-2, 
					qualifierLineBuilder.length());
			qualifierLineBuilder.append(")");
		}
		return qualifierLineBuilder.toString();
	}
	
	private static Set<String> extractStereotypes(IClassBean bean) {
		Set<String> result = new HashSet<>();
		for (IStereotypeDeclaration declaration : bean.getStereotypeDeclarations()) {
			String stereotypeName = extractAnnotation(declaration);
			result.add(stereotypeName);
		}
		return result;
	}

	private static Scope extractScope(IClassBean bean) {
		boolean hasScopeDeclaration = hasScopeDeclaration(bean);
		if (hasScopeDeclaration) {
			IScope scope = bean.getScope();
			String scopeName = scope.getSourceType().getFullyQualifiedName();
			Scope result = new Scope(scopeName, scope.isNorlmalScope());
			return result;
		} else {
			return Scope.UNDEFINED;
		}
	}

	private static boolean hasScopeDeclaration(IClassBean bean) {
		for (IScopeDeclaration declaration : bean.getScopeDeclarations()) {
			if (declaration != null) {
				return true;
			}
		}
		return false;
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
