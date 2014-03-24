package cz.muni.fi.cdii.webapp;

import javax.inject.Inject;
import javax.inject.Named;
	
@Named
public class HelloBean {
	
	@Inject
	@SimpleQualifier
	private Bean1 bean1;
	
	@Inject
	@ParamQualifier(value = 3.14f, numbers = {3})
	private Bean1 bean2;
   
   public String getHello() {
	   return "Hello world!";
   }
	
	public Bean1 getBean1() {
		return bean1;
	}
	
	public Bean1 getBean2() {
		return bean2;
	}
	

}
