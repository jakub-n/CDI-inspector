package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.Bean;
import cz.muni.fi.cdii.common.model.Member;

public interface GraphMember {
    
    @Property("origin")
    public Member getOrigin();

    @Property("origin")
    public void setOrigin(Member origin);
    
    @Adjacency(label="produces", direction=Direction.OUT)
    public Bean getProduced();

    @Adjacency(label="produces", direction=Direction.OUT)
    public void setProduced(Bean bean);

    @Adjacency(label="hasInjectionPoints", direction=Direction.OUT)
    public Iterable<GraphInjectionPoint> getInjectionPoints();
    
    // TODO add some setter for injection point
}
