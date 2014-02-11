package cz.muni.fi.cdii.poc.jsonserialization.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Car {

	@JsonProperty
	protected int numOfDoors;
	
	@JsonProperty
	protected int numOfWheels;
	
	public Car() {
		System.out.println("init 0");
		this.numOfDoors = 1;
		this.numOfWheels = 1;
	}
	
	public Car(Object nothing) {
		System.out.println("init obj");
		this.numOfDoors = 7;
		this.numOfWheels = 4;
	}

	public int getNumOfDoors() {
		System.out.println("getNumOfDoors");
		return numOfDoors;
	}

	public int getNumOfWheels() {
		System.out.println("getNumOfWheels");
		return numOfWheels;
	}
	
	@Override
	public String toString() {
		return "Car [numOfDoors=" + this.numOfDoors + ", numOfWheels=" + this.numOfWheels + "]"; 
	}
	
}

// TODO castecny konstruktor - pripadne delegat = mapa
// TODO identita
// TODO zpetne a cyklicke reference
// TODO pres settery a gettry nebo pres vlastnosti? -

