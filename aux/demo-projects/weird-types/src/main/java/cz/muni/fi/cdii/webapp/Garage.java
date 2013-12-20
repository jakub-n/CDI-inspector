package cz.muni.fi.cdii.webapp;

import java.util.HashSet;
import java.util.Set;

public class Garage<T extends WheelVehicle> {
	Set<T> vehicles = new HashSet<T>();
	
	public void park(T vehicle) {
		this.vehicles.add(vehicle);
	}

}
