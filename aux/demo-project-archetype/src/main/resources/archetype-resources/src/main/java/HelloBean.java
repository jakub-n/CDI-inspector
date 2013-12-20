#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import javax.inject.Named;
	
@Named
public class HelloBean {
   
   public String getHello() {
	   return "Hello world!";
   }

}
