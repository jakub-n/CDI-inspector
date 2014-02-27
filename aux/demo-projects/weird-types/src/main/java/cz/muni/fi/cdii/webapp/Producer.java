package cz.muni.fi.cdii.webapp;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import cz.muni.fi.cdii.webapp.aux.Car;
import cz.muni.fi.cdii.webapp.aux.FastCar;
import cz.muni.fi.cdii.webapp.aux.Garage;
import cz.muni.fi.cdii.webapp.aux.Season;
import cz.muni.fi.cdii.webapp.aux.WheelVehicle;
	
public class Producer {
	
	@Named
	@Produces
	public static final int intField = 8;
	
	@Named
	@Produces
	public static final Integer integerField = 9;
   
	@Named
	@Produces
   public static final String string = "Hello world!";
   
	@Named
	@Produces
	public static final String[] arrayOfStrings = new String[]{"hello", "world"};
	
	@Named
	@Produces
	public static final Season enumField = Season.SPRING;
	
	@Named
	@Produces
	public static final Map<String ,Garage<Car>> getNamedGarages() {
		Map<String ,Garage<Car>> garages = new HashMap<>();
		Garage<Car> myGarage = new Garage<>();
		myGarage.park(new Car());
		garages.put("my", myGarage);
		return garages;
	}
	
	@Named
	@Produces
	public static final WheelVehicle interfaceImpl = new WheelVehicle() {
		
		@Override
		public int getWheelNumber() {
			return 3;
		}
	};
	
	@Named
	@Produces
	public static final Object objectImpl = new Object() {
		@Override
		public String toString() {
			return "objectImpl";
		};
	};
	
	@Named
	@Produces
	public static final FastCar inheritingObject = new FastCar();
	
	@Named
	@Produces
	public String getStringMethod() {
		return "Hello world method";
	}

}
