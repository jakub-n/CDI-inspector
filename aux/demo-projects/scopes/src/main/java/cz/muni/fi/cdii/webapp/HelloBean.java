package cz.muni.fi.cdii.webapp;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
	
@Named
@Dependent
public class HelloBean {
   
   public String getHello() {
	   return "Hello world!";
   }

}
