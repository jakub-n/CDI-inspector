package cz.muni.fi.cdii.webapp;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class Producer {

	@Produces
	@Named
	public String getHello() {
		return "Hello world!";
	}

}
