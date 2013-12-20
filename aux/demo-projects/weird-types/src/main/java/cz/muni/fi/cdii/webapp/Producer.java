package cz.muni.fi.cdii.webapp;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
	
public class Producer {
	
	@Named
	@Produces
	public static final int number = 8;
   
	@Named
	@Produces
   public static final String string = "Hello world!";
   
	@Named
	@Produces
   public String[] strings = new String[]{"hello", "world"};
	
	@Named
	@Produces
	public Season spring = Season.SPRING;
	
	@Named
	@Produces
	public Map<String ,Garage<Car>> namedGarages() {
		Map<String ,Garage<Car>> garages = new HashMap<>();
		Garage<Car> myGarage = new Garage<>();
		myGarage.park(new Car());
		garages.put("my", myGarage);
		return garages;
	}

}
