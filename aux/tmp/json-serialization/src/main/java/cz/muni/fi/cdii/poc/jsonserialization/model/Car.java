package cz.muni.fi.cdii.poc.jsonserialization.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
	
	@JsonProperty
	@JsonBackReference("garage-car")
	protected Garage garage;
	
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
	
	public Garage getGarage() {
		return garage;
	}

	public void setGarage(Garage garage) {
		this.garage = garage;
	}
	
	protected String toRawString() {
		try {
			return " [numOfDoors=" + this.numOfDoors + ", numOfWheels=" + this.numOfWheels 
					+ ", garage=" + this.garage.hashCode() + "]";
		} catch (IllegalArgumentException | SecurityException e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public String toString() {
		return "Car" + this.toRawString();
		
	}
	
}

// TODO castecny konstruktor - pripadne delegat = mapa - castecny konstruktor funguje
// TODO identita              @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
// TODO zpetne a cyklicke reference  @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
// TODO pres settery a gettry nebo pres vlastnosti? - OD nastavitelne pres @JsonAutoDetect

