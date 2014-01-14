package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

/**
 * Represents entity 1:1 matching to some *.class file
 *
 */
public class Class {

	/**
	 * can be null if anonymous
	 */
	private String name;
	
	/**
	 * OuterClass$ThisClass
	 */
	private Class outerClass;
	
	private Package packag;
	
	private Set<Field> fields;
	
	// TODO is constructor
	private Set<Method> methods;
	
	// class member
	private Set<InjectionPoint> injectionPoints;
	
	/**
	 * based on {@link java.lang.Class#getName()}
	 * <br /> this field is used by {@link #equals(Object)} and {@link #hashCode()} methods
	 */
	private String internalName;
	
	public Set<Class> getEffectiveTypeSet() {
		// TODO implement
		throw new UnsupportedOperationException();
	}
	
	// TODO prepsat hashcode and equals
	// TODO zatim neni jak rozlisit 2 anonymni tridy implementujici stejne rozhrani
}
