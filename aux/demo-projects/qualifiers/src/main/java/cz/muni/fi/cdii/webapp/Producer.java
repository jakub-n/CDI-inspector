package cz.muni.fi.cdii.webapp;

import javax.enterprise.inject.Produces;

public class Producer {

	@Produces
	@ParamQualifier(numbers = {3}, value = 3.14f)
	Bean1 getBean1() {
		return new Bean1();
	}
}
