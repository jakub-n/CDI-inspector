package cz.muni.fi.cdii.plugin;

import java.util.Collection;
import java.util.Collections;

import cz.muni.fi.cdii.plugin.model.IBean;

public class LocalBean implements IBean {
	
	private org.jboss.tools.cdi.core.IBean jbossBean;
	
	public LocalBean(org.jboss.tools.cdi.core.IBean jbossBean) {
		this.jbossBean = jbossBean;
	}

	@Override
	public String getQalifiedName() {
		return this.jbossBean.getName();
	}

	@Override
	public Collection<IBean> getInjectedIntoNodes() {
		return Collections.emptySet();
	}

	@Override
	public Collection<IBean> getInjectedNodes() {
		return Collections.emptySet();
	}

	@Override
	public boolean isOpenable() {
		return true;
	}

	@Override
	public void open() {
		this.jbossBean.open();
	}

	@Override
	public String toString() {
		return "{" + this.getQalifiedName() + "}";
	}
	
	

}
