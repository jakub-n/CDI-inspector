package cz.muni.fi.cdii.webapp;

import javax.inject.Named;
	
@Named
public class HelloBean {
   
   public String getHello() {
	   return "Hello world!";
   }

}
