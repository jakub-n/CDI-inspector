package cz.muni.fi.cdii.webapp;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Produces;

public class Producer {

	@Produces
	public Set set = new HashSet();
	
	@Produces
	public ParamProducer<Integer> intSetProduces = new ParamProducer<Integer>("Integer");
}
