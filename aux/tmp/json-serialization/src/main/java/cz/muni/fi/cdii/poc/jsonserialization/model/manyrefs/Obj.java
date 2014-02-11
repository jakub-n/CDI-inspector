package cz.muni.fi.cdii.poc.jsonserialization.model.manyrefs;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@JsonIgnoreProperties(ignoreUnknown=true)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Obj {
	
	@JsonProperty
	public String name;

	@JsonProperty
	public Obj obj1;
	
	@JsonProperty
	public Obj obj2;
	
	@JsonProperty
	public Container parent;
	
	@Override
	public String toString() {
		return "Obj " + this.hashCode() + " " + this.name + " [obj1=" 
				+ ((obj1==null) ? "null" : obj1.hashCode() + " " + obj1.name) + ", obj2=" 
				+ ((obj2==null) ? "null" : obj2.hashCode() + " " + obj2.name) + ", parent=" 
				+ ((parent==null) ? "null" : parent.hashCode()) + "]";
	}
	
}
