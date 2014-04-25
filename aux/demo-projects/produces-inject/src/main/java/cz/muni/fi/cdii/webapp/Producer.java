package cz.muni.fi.cdii.webapp;

import java.util.Collections;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class Producer {
	
	@Produces
	@Named(":-)")
	public Set<Set<Integer>> getBean() {
		Set<Set<Integer>> result = Collections.emptySet();
		return result;
	}

}
