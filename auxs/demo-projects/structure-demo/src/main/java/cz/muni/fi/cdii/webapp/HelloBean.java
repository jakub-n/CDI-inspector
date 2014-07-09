package cz.muni.fi.cdii.webapp;

import javax.inject.Inject;
import javax.inject.Named;
	
@Named
public class HelloBean {
   
	@Inject
	private int number;
	
	@Inject
	private void argumentInjection(int num) {}
	
	
   public String getHello() {
	   return "Hello world!";
   }

}
