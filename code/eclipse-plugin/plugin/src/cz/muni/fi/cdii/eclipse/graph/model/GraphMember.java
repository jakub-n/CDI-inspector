package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.Member;

public interface GraphMember extends GraphElement {
    
    @Property("origin")
    public Member getOrigin();

    @Property("origin")
    public void setOrigin(Member origin);
    
    @Adjacency(label="produces", direction=Direction.OUT)
    public GraphBean getProducedBean();

    @Adjacency(label="produces", direction=Direction.OUT)
    public void setProducedBean(GraphBean bean);

    @Adjacency(label="hasInjectionPoint", direction=Direction.OUT)
    public Iterable<GraphInjectionPoint> getInjectionPoints();
    
    @Adjacency(label="hasInjectionPoint", direction=Direction.OUT)
    public void setInjectionPoints(Iterable<GraphInjectionPoint> injectionPoints);
}
