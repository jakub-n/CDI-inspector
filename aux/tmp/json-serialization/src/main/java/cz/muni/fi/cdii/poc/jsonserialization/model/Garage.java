package cz.muni.fi.cdii.poc.jsonserialization.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Garage {

	@JsonProperty
	public Car car;
	
	@Override
	public String toString() {
		return "Garage [car=" + this.car + "]";
	}
}
