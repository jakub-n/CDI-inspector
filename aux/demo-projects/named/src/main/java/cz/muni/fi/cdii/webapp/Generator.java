package cz.muni.fi.cdii.webapp;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class Generator {

    @Produces
    public String str1 = "without @Named";
    
    @Produces
    @Named
    public String str2 = "no name value";
    
    @Produces
    @Named("hello")
    public String str3 = "name hello";
    
    
}
