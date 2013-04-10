package cz.muni.fi.cdii.plugin.model;

import java.util.Collection;

public interface IBean {
	
	public String getQalifiedName();
	public Collection<IBean> getInjectedIntoNodes();
	public Collection<IBean> getInjectedNodes();
	public boolean isOpenable();
	public void open();
}
