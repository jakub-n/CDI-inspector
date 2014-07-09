package cz.muni.fi.cdii.poc.jsonserialization;

import java.io.IOException;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cz.muni.fi.cdii.poc.jsonserialization.model.Car;
import cz.muni.fi.cdii.poc.jsonserialization.model.Garage;
import cz.muni.fi.cdii.poc.jsonserialization.model.LocalCar;

public class Main {

	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule localCarModule = new SimpleModule("local-car-module", 
				new Version(1, 0, 0, "", "cz.muni.fi.cdii.jackson", "local-car"));
		localCarModule.setMixInAnnotation(Car.class, LocalCar.class);
		mapper.registerModule(localCarModule);
		
		

		Car car = new Car(null);
//		Car car = new LocalCar();
		Garage garage = new Garage(1, 2);
		car.setGarage(garage);
		garage.car = car;
		
		String json = mapper.writeValueAsString(garage);
		System.out.println(json);
		
		Garage readGarage = mapper.readValue(json, Garage.class);
		System.out.println(readGarage);
	}

}
