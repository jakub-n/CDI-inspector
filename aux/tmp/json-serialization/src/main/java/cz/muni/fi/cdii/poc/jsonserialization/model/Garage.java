package cz.muni.fi.cdii.poc.jsonserialization.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility=Visibility.DEFAULT, setterVisibility=Visibility.NONE, 
	getterVisibility=Visibility.NONE, isGetterVisibility=Visibility.NONE)
public class Garage {
	
	@JsonProperty
	private Object obj1;
	
	@JsonProperty
	private Object obj2;
	
	@JsonCreator
	public Garage(@JsonProperty("obj1") final Object obj1) {
		this.obj1 = obj1;
	}
	public Garage(Object obj1, Object obj2) {
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@JsonProperty
	@JsonManagedReference("garage-car")
	public Car car;
	
	@Override
	public String toString() {
		return "Garage [car=" + this.car + ", obj1=" + this.obj1 + ", obj2=" +this.obj2 + "]";
	}
}
