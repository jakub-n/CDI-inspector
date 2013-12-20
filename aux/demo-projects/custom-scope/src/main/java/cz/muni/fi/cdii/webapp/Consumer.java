package cz.muni.fi.cdii.webapp;

import javax.inject.Inject;
import javax.inject.Named;
	
@Named
public class Consumer {
	
	@Inject
	@MyScoped
	private Bean bean;
   
   public String getHello() {
	   System.out.println("Consumer.getHello: " + bean.toString());
	   return bean.getHello();
   }

}
