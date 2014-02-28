package cz.muni.fi.cdii.common.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InjectionPoint {
	

	@JsonProperty
	private Set<Qualifier> qualifiers;

	/**
	 * typically contains exactly one element
	 */
	@JsonProperty
	private Set<Bean> resolvedBeans = new HashSet<>();
	
	private String elName;

    public Set<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Set<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Set<Bean> getResolvedBeans() {
        return resolvedBeans;
    }

    public void setResolvedBeans(Set<Bean> resolvedBeans) {
        this.resolvedBeans = resolvedBeans;
    }

    public String getElName() {
        return elName;
    }

    public void setElName(String elName) {
        this.elName = elName;
    }
}