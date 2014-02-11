package cz.muni.fi.cdii.poc.jsonserialization.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=LocalCar.class)
public class LocalCar extends Car {

	@Override
	public String toString() {
		return "Local Car" + this.toRawString();
	}
}
