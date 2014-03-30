package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.Bean;


public interface GraphBean {
    
    @Property("origin")
    public Bean getOrigin();

    @Property("origin")
    public void setOrigin(Bean origin);
    
    @Property("elName")
    public String getElName();
    
    @Property("elName")
    public void setElName(String elName);

    @Adjacency(label="scope", direction=Direction.OUT)
    public GraphScope getScope();
    
    @Adjacency(label="scope", direction=Direction.OUT)
    public void setScope(GraphScope scope);
    
    @Adjacency(label="mainType", direction=Direction.OUT)
    public GraphType getMainType();
    
    @Adjacency(label="mainType", direction=Direction.OUT)
    public void setMainType();
    
    @Adjacency(label="typeSet", direction=Direction.OUT)
    public Iterable<GraphType> getTypeSet();
    
    @Adjacency(label="typeSet", direction=Direction.OUT)
    public void setTypeSet(Iterable<GraphType> typeSet);
}
