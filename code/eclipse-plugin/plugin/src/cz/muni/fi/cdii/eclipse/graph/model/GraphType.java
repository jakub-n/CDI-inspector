package cz.muni.fi.cdii.eclipse.graph.model;

import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.annotations.gremlin.GremlinGroovy;
import com.tinkerpop.frames.modules.javahandler.Initializer;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerClass;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;

import cz.muni.fi.cdii.common.model.Type;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElement;

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
    
    @Adjacency(label="mainType", direction=Direction.IN)
    public Iterable<GraphBean> getBeans();
    
    @GremlinGroovy("it.out('hasMember').out('produces').dedup()")
    public Iterable<GraphBean> getAuxiliaryProducesTargets();
    
    @JavaHandler
    public DetailsElement getDetails();
    
    @JavaHandler
    public String getDetailsLinkLabel();
    
    public static abstract class Impl implements JavaHandlerContext<Vertex>, GraphType {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
        
        public DetailsElement getDetails() {
            DetailsElement root = new DetailsElement();
            root.addSubElement(new DetailsElement("Package:", this.getOrigin().getPackage()));
            root.addSubElement(new DetailsElement("Name:", this.getOrigin().getName()));
            root.addSubElement(new DetailsElement("Is array", 
                    this.getOrigin().isArray() ? "true" : "false"));
            
            DetailsElement typeParamsRoot = new DetailsElement("Type parameters", 
                    this.getOrigin().getTypeParameters().isEmpty() ? "<none>" : "");
            for (Type type : this.getOrigin().getTypeParameters()) {
                typeParamsRoot.addSubElement(new DetailsElement("", type.toString(true, true)));
            }
            root.addSubElement(typeParamsRoot);
            
            List<GraphBean> beans = Utils.iterableToList(this.getBeans());
            DetailsElement rootBeans = new DetailsElement("Beans", beans.isEmpty() ? "<none>" : "");
            for (GraphBean bean : beans) {
                rootBeans.addSubElement(new DetailsElement("", bean));
            }
            root.addSubElement(rootBeans);
            return root;
        }
        
        public String getDetailsLinkLabel() {
            return this.getOrigin().toString(false, true);
        }
    }
}
