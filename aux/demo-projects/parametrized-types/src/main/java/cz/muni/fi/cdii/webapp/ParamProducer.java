package cz.muni.fi.cdii.webapp;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class ParamProducer<T> {
	
	
	
//	public ParamProducer() {
//		System.out.println("ParamProducer constructor ***********");
//	}
	
	public ParamProducer(String message) {
		System.out.println("ParamProducer constructor " + message + " ***********");
	}

	@Produces
	public Set<T> producingMethod(InjectionPoint ip) {
		System.out.println("producing method called for ip: " + ip.getType());
		Set<T> result= new HashSet<>();
		return result;
	}
	
//	public void setField(final T field) {
//		this.producingField.add(field);
//	}
}
