package cz.muni.fi.cdii.eclipse.graph.model;

import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.modules.javahandler.Initializer;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;

import cz.muni.fi.cdii.common.model.Bean;

@JavaHandlerClass(GraphBean.Impl.class)
public interface GraphBean extends VertexFrame, GraphElement {
    
    public static final String VERTEX_TYPE_NAME = "bean";
    
    @Property(Constants.VERTEX_TYPE_PROPERTY)
    public String getVertexType();
    
    @Property("origin")
    public Bean getOrigin();

    @Property("origin")
    public void setOrigin(Bean origin);
    
    @Property("elName")
    public String getElName();
    
    @Property("elName")
    public void setElName(String elName);
    
    @Property("qualifiers")
    public List<String> getQualifiers();
    
    @Property("qualifiers")
    public void setQualifiers(List<String> qualifiers);
    
    @Property("scope")
    public String getScope();

    @Property("scope")
    public void setScope(String scope);
    
    @Adjacency(label="mainType", direction=Direction.OUT)
    public GraphType getMainType();
    
    @Adjacency(label="mainType", direction=Direction.OUT)
    public void setMainType(GraphType mainType);
    
    @Adjacency(label="typeSet", direction=Direction.OUT)
    public Iterable<GraphType> getTypeSet();
    
    @Adjacency(label="typeSet", direction=Direction.OUT)
    public void setTypeSet(Iterable<GraphType> typeSet);
    
    @Adjacency(label="injectedInto", direction=Direction.OUT)
    public Iterable<GraphInjectionPoint> getInjectionPoints();
        
    @GremlinGroovy("it.out('injectedInto').in('hasInjectionPoint').dedup()")
    public Iterable<GraphMember> getInjectionTargetMembers();
    
    /**
     * @return types that has some member hat injects this bean
     */
    @GremlinGroovy("it.out('injectedInto').in('hasInjectionPoint').in('hasMember').dedup()")
    public Iterable<GraphType> getAuxiliaryInjectionTargetTypes();
    
    @JavaHandler
    public String getTooltipText();
    
    public static abstract class Impl implements JavaHandlerContext<Vertex>, GraphBean {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
        
        // TODO upravit
        public String getTooltipText() {
            return this.getOrigin().getType().toString(true, true);
        }
    }
}
