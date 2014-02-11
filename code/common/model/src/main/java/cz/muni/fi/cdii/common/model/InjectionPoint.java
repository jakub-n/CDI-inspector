package cz.muni.fi.cdii.common.model;

import java.util.Set;

public class InjectionPoint {
	
	private InjectableLocation location;
	private Set<Qualifier> effectiveQualifiers;
	private Bean resolvedBean;
}
