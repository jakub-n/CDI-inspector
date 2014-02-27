package cz.muni.fi.cdii.webapp.aux;

import javax.inject.Named;

@Named
public class Car implements WheelVehicle {

	@Override
	public int getWheelNumber() {
		return 4;
	}

}
