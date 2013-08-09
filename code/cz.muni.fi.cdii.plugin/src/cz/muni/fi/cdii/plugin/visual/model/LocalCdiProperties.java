package cz.muni.fi.cdii.plugin.visual.model;

import java.util.Set;

import cz.muni.fi.cdii.plugin.common.model.CdiProperties;
import cz.muni.fi.cdii.plugin.common.model.Scope;

public class LocalCdiProperties extends CdiProperties {
	
	private Scope scope;
	private String name;
	private Set<String> qualifiers;
	private Set<String> stereotypes;

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Scope getScope() {
		return this.scope;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<String> getQualifiers() {
		return this.qualifiers;
	}

	@Override
	public Set<String> getStereotypes() {
		return this.stereotypes;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQualifiers(Set<String> qualifiers) {
		this.qualifiers = qualifiers;
	}

	public void setStereotypes(Set<String> stereotypes) {
		this.stereotypes = stereotypes;
	}

}
