package cz.muni.fi.cdii.common.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

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
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Bean {

	@JsonProperty
	private Type type;
	
	@JsonProperty
	private Set<Type> typeSet;
	
	@JsonProperty
	private Set<Qualifier> effectiveQualifierSet;
	
	@JsonProperty
	private Scope scope;
	
	@JsonProperty
	private String elName;
	
	@JsonProperty
	private Set<InterceptorBinding> interceptorBindings;
	
	@JsonProperty
	private Set<Delegate> associatedDeletates;
	
}
