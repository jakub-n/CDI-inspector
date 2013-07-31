package cz.muni.fi.cdii.plugin.visual.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.core.ICDIProject;
import org.jboss.tools.cdi.core.IClassBean;

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

		for (IBean bean : cdiProject.getBeans()) { // IProducerField, IProducerMethod, ClassBean, BuiltInBean
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

	private void processIClassBean(IClassBean bean) {
		LocalClass localClass = new LocalClass();
		
		String qualifiedName = this.getFullyQualifiedName(bean.getBeanClass());
		int lastDotIndex = qualifiedName.lastIndexOf(".");
		String packageName = qualifiedName.substring(0, lastDotIndex);
		String className = qualifiedName.substring(lastDotIndex + 1, qualifiedName.length());
		localClass.setPackageName(packageName);
		localClass.setClassName(className);
		this.classes.add(localClass);
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
