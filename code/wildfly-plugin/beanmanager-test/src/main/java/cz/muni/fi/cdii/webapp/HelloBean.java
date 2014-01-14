package cz.muni.fi.cdii.webapp;

import java.util.Date;

import javax.inject.Named;
	
@Named
public class HelloBean {
   
   public String getHello() {
	   return new Date().toString();
   }

}
