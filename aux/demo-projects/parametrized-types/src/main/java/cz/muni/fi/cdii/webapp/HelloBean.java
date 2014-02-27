package cz.muni.fi.cdii.webapp;

import java.util.Date;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import cz.muni.fi.cdii.webapp.inheritance.Animal;

@Named
public class HelloBean<T extends Date> {

	// @Inject
	private Set<T> set;

	@Inject
	private Set<Integer> intSet;

	@Inject
	private Animal animal;

	public String getHello() {
		return "set: " + (this.set == null ? "null" : this.set)
				+ System.lineSeparator() + (this.intSet == null ? "null" : this.intSet)
				+ System.lineSeparator() + new Date();
	}

}
