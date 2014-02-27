package cz.muni.fi.cdii.webapp;

import javax.enterprise.inject.Produces;

public class Producer {

	@Produces
	public static final int number = 8;
}
