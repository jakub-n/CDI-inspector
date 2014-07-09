package cz.muni.fi.cdii.poc.jsonserialization.model.manyrefs;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Container {

	@JsonProperty
	public Set<Obj> objs;
	
	public void generate() {
		this.objs = new HashSet<>();
		Obj objA = new Obj();
		objA.name = "A";
		Obj objB = new Obj();
		objB.name = "B";
		Obj objC = new Obj();
		objC.name = "C";
		
		objA.parent = this;
		objB.parent = this;
		objC.parent = this;
		
		objA.obj1 = objB;
		objA.obj2 = objC;
		objB.obj1 = objA;
		objB.obj2 = objC;
		objC.obj1 = objA;
		objC.obj2 = objB;
		
		this.objs.add(objA);
		this.objs.add(objB);
		this.objs.add(objC);
	}
	
	@Override
	public String toString() {
		return "container " + this.hashCode() + " " + this.objs.toString(); 
	}
}
