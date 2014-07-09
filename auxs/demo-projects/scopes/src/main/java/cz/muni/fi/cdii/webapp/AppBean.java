package cz.muni.fi.cdii.webapp;

import javax.enterprise.context.ApplicationScoped;
	
@ApplicationScoped
public class AppBean {
   
   public String getHello() {
	   return "Hello world!";
   }

}
