package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.Type;

public interface GraphType {

    @Property("origin")
    public Type getOrigin();

    @Property("origin")
    public void setOrigin(Type origin);
    
    @Property("package")
    public String getPackage();
    
    @Property("package")
    public void setPackage(String package_);
    
    @Property("name")
    public String getName();

    @Property("name")
    public void setName(String name);
}
