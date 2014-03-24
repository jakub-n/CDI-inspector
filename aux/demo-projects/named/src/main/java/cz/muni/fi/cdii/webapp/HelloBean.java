package cz.muni.fi.cdii.webapp;

import javax.inject.Inject;
import javax.inject.Named;
	
@Named
public class HelloBean {
    
    @Inject
    private String str1;
    
    @Inject
    @Named
    private String str2;
    
    @Inject
    @Named("hello")
    private String str3;
   
   public String getHello() {
	   return "Hello world!";
   }

}
