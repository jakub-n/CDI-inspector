package cz.muni.fi.cdii.webapp;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class Producer {
	
	@Produces
	@Named(":-)")
	public HelloBean getBean() {
		return new HelloBean();
	}
	
	@Produces
	public Set<Set<String>> setOfSetsOfStrings = new HashSet<>(); 

}
