package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

public class InjectionPoint {
	
	private InjectableLocation location;
	private Set<Qualifier> effectiveQualifiers;
	private Bean resolvedBean;
}
