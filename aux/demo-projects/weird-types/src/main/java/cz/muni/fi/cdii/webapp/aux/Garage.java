package cz.muni.fi.cdii.webapp.aux;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Garage<T extends WheelVehicle> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Set<T> vehicles = new HashSet<T>();
	
	public void park(T vehicle) {
		this.vehicles.add(vehicle);
	}

}
