package cz.muni.fi.cdii.eclipse.graph.model;

import java.util.List;

import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.InjectionPoint;

public interface GraphInjectionPoint {

    @Property("origin")
    public InjectionPoint getOrigin();

    @Property("origin")
    public void setOrigin(InjectionPoint origin); 
    
    @Property("qualifiers")
    public List<String> getQualifiers();
    
    @Property("qualifiers")
    public void setQualifiers(List<String> qualifiers);
    
    @Property("elName")
    public String getElName();
    
    @Property("elName")
    public void setElName(String elName);
}
