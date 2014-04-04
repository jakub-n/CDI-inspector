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
// TODO likely delete
@Deprecated
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Class {
	
	/**
	 * can be null if anonymous
	 */
	// TODO v pripade anonymni tridy zmenit na cislo
	@JsonProperty
	private String name;
	
	/**
	 * OuterClass$ThisClass
	 * <p> {@code null} if this class is not nested
	 */
	@JsonProperty
	private Class outerClass;

	/**
	 * null if this class is a primitive type
	 */
	@JsonProperty
	private String package_;
	
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getOuterClass() {
		return outerClass;
	}

	public void setOuterClass(Class outerClass) {
		this.outerClass = outerClass;
	}

	public String getPackage_() {
		return package_;
	}

	public void setPackage_(String package_) {
		this.package_ = package_;
	}

	public Set<Field> getFields() {
		return fields;
	}

	public void setFields(Set<Field> fields) {
		this.fields = fields;
	}

	public Set<Method> getMethods() {
		return methods;
	}

	public void setMethods(Set<Method> methods) {
		this.methods = methods;
	}

	public Set<InjectionPoint> getInjectionPoints() {
		return injectionPoints;
	}

	public void setInjectionPoints(Set<InjectionPoint> injectionPoints) {
		this.injectionPoints = injectionPoints;
	}

	public String getJavaName() {
		return javaName;
	}

	@Override
	public String toString() {
		return this.outerClass == null
				? ((this.package_ != null ? this.package_ : "")
						+ "."
						+ (this.name != null ? this.name : "-anonym-"))
				: super.toString() + "$" + (this.name != null ? this.name : "-anonym-");
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
