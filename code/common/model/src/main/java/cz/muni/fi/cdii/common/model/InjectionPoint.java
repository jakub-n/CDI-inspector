package cz.muni.fi.cdii.common.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InjectionPoint {
	

	@JsonProperty
	private InjectableLocation location;

	@JsonProperty
	private Set<Qualifier> effectiveQualifiers;

	@JsonProperty
	private Bean resolvedBean;
}
