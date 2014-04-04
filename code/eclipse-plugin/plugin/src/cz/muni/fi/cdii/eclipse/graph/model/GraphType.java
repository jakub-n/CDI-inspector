package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.modules.javahandler.Initializer;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;

import cz.muni.fi.cdii.common.model.Type;

@JavaHandlerClass(GraphType.Impl.class)
public interface GraphType extends GraphElement {

    public static final String VERTEX_TYPE_NAME = "type";
    
    @Property(VERTEX_TYPE_NAME)
    public String getVertexType();

    @Property("origin")
    public Type getOrigin();

    @Property("origin")
    public void setOrigin(Type origin);
    
    /**
     * e.g. "java.lang"
     */
    @Property("package")
    public String getPackage();
    
    @Property("package")
    public void setPackage(String package_);
    
    /**
     * e.g. "HashSet"
     */
    @Property("name")
    public String getName();

    @Property("name")
    public void setName(String name);

    /**
     * e.g. "java.util.HashSet&lt;java.lang.String&gt;" 
     */
    @Property("completeName")
    public String getCompleteName();

    @Property("completeName")
    public void setCompleteName(String completeName);
    
    @Adjacency(label="hasMember", direction=Direction.OUT)
    public Iterable<GraphMember> getMembers();
    
    @Adjacency(label="hasMember", direction=Direction.OUT)
    public void setMembers(Iterable<GraphMember> member);
    
    public static abstract class Impl implements JavaHandlerContext<Vertex>, GraphType {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
    }
}
