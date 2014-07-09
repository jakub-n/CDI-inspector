package cz.muni.fi.cdii.webapp;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Consumer {

	@Inject
	private HelloBean bean;

	public HelloBean getBean() {
		return bean;
	}
}
