package cz.muni.fi.cdii.eclipse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jboss.tools.cdi.core.ICDIProject;

import cz.muni.fi.cdii.plugin.model.IBean;
import cz.muni.fi.cdii.plugin.model.IInspection;

@Deprecated
public class LocalInspection implements IInspection {
	
	private Set<IBean> beans = new HashSet<IBean>();
	
	public LocalInspection(ICDIProject project) {
		this.generateBeans(project);
	}
	
	private void generateBeans(ICDIProject project) {
		for (org.jboss.tools.cdi.core.IBean bean : project.getBeans()) {
			LocalBean localBean = new LocalBean(bean);
			this.beans.add(localBean);
		}
	}

	@Override
	public Collection<IBean> getBeans() {
		return this.beans;
	}

	@Override
	public String toString() {
		return "beans: " + beans.toString();
	}

}
