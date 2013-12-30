package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

public class Bean {

	private Type type;
	private Set<Type> typeSet;
	private Set<Qualifier> effectiveQualifierSet;
	private Scope scope;
	private String elName;
	private Set<InterceptorBinding> interceptorBindings;
	private Set<Delegate> associatedDeletates;
	
}
