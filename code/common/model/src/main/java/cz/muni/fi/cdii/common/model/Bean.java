package cz.muni.fi.cdii.common.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Something that can by injected and/or is produced.
 * Set of objects uniquely identified by 
 * <ul>
 * <li> nonempty set of types
 * <li> nonempty set of qualifiers
 * <li> scope
 * <li> EL name (optional)
 * <li> set of interceptor bindings
 * </ul>
 * Beans also may have alternatives.
 */
public class Bean {

	private Type type;
	private Set<Type> typeSet;
	private Set<Qualifier> effectiveQualifierSet;
	private Scope scope;
	private String elName;
	private Set<InterceptorBinding> interceptorBindings;
	private Set<Delegate> associatedDeletates;
	
}
