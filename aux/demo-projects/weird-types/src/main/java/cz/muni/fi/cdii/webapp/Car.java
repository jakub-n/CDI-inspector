package cz.muni.fi.cdii.webapp;

import javax.inject.Named;

@Named
public class Car implements WheelVehicle {

	@Override
	public int getWheelNumber() {
		return 4;
	}

}
