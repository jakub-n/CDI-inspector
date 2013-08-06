package cz.muni.fi.cdii.plugin.visual.model;

import java.util.Collections;
import java.util.Set;

import cz.muni.fi.cdii.plugin.common.model.CdiProperties;
import cz.muni.fi.cdii.plugin.common.model.Scope;

public class LocalCdiProperties extends CdiProperties {
	
	private Scope scope;

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Scope getScope() {
		return this.scope;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "name not implemented";
	}

	@Override
	public String getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getQualifiers() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public Set<String> getStereotypes() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

}
