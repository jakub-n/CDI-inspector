package cz.muni.fi.cdii.webapp;

import javax.enterprise.inject.Model;

@SimpleQualifier
public class Bean1 {

	private static final String HELLO = "hello";

	public String getHello() {
		return HELLO;
	}
}
