package cz.muni.fi.cdii.webapp;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Consumer {

	@Inject
	@Named(":-)")
	private HelloBean bean;
	
	@Inject
	public Set<Set<String>> set;

	public HelloBean getBean() {
		return bean;
	}
}
