package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.frames.Property;

public interface GraphScope {
    
    @Property("builtin")
    public boolean isBuiltIn();
    
    @Property("builtin")
    public void setBuiltIn(boolean isBuiltIn);

    @Property("name")
    public String getName();
    
    @Property("name")
    public void setName(String name);

}
