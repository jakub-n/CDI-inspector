package cz.muni.fi.cdii.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Represents entity 1:1 matching to some *.class file
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Class {
	
	public static final String IOPENABLE = "org.eclipse.jdt.core.IOpenable";

	/**
	 * can be null if anonymous
	 */
	@JsonProperty
	private String name;
	
	/**
	 * OuterClass$ThisClass
	 */
	@JsonProperty
	private Class outerClass;

	@JsonProperty
	private Package package_;
	
	/**
	 * Value is obtained by calling {@link java.lang.Class#getName()}.
	 * <p>
	 * {@link #equals(Object)} and {@link #hashCode()} are based on this field
	 */
	// TODO mozna final
	@JsonProperty
	private String javaName;

	@JsonProperty
	private Set<Field> fields;
	
	// TODO is constructor
	@JsonProperty
	private Set<Method> methods;
	
	// class member
	@JsonProperty
	private Set<InjectionPoint> injectionPoints;
	
	// TODO mozna vytvorit jen lokalniho typove bezpecneho potomka Class a vnem vlastnost
	// TODO neserializovat
	@JsonProperty
	private Map<String, Object> actions = new HashMap<String, Object>();
	
	public Class(final java.lang.Class<?> clazz) {
		this.javaName = clazz.getName();
	}
	
	/**
	 * 
	 * @param name as {@link java.lang.Class#getName()} would return
	 */
	public Class(final String name) {
		this.javaName = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((javaName == null) ? 0 : javaName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Class other = (Class) obj;
		if (javaName == null) {
			if (other.javaName != null)
				return false;
		} else if (!javaName.equals(other.javaName))
			return false;
		return true;
	}


}
