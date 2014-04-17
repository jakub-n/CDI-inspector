package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.modules.javahandler.Initializer;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;

import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterModel;

@JavaHandlerClass(GraphMember.Impl.class)
public interface GraphMember extends GraphElement {
    
    public static final String VERTEX_TYPE_NAME = "member";
    
    @Property(Constants.VERTEX_TYPE_PROPERTY)
    public String getVertexType();
    
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
    
    @GremlinGroovy(value="true", frame=false)
    public boolean satisfies(FilterModel criteria);
    
    
    public static abstract class Impl implements GraphMember, JavaHandlerContext<Vertex> {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
    }
}
