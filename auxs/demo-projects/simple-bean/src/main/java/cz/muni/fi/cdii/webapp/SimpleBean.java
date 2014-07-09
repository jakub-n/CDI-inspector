package cz.muni.fi.cdii.webapp;

import javax.inject.Named;
	
@Named
public class SimpleBean {
   
   public String getHello() {
	   return "Hello world!";
   }

}
