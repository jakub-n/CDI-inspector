package cz.muni.fi.cdii.plugin.visual.model;

import java.util.Set;

import cz.muni.fi.cdii.plugin.common.model.Bean;
import cz.muni.fi.cdii.plugin.common.model.BeanField;
import cz.muni.fi.cdii.plugin.common.model.BeanFunction;
import cz.muni.fi.cdii.plugin.common.model.CdiProperties;
import cz.muni.fi.cdii.plugin.common.model.Class;

public class LocalBean extends Bean {
	
	final private LocalClass clazz;
	private LocalCdiProperties properties;
	
	public LocalBean(LocalClass clazz) {
		this.clazz = clazz;
		this.clazz.addBean(this);
	}

	@Override
	public Class getClazz() {
		return this.clazz;
	}

	@Override
	public CdiProperties getBeanProperties() {
		return this.properties;
	}

	@Override
	public Set<BeanField> getInjectedFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<BeanFunction> getInitializerFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProperties(LocalCdiProperties properties) {
		this.properties = properties;
	}

}
