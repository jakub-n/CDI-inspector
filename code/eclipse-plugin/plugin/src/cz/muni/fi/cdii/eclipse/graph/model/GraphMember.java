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

import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.common.model.Method;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElement;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElementFactory;
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
    
    @Adjacency(label="hasMember", direction=Direction.IN)
    public GraphType getSurroundingType();
    
    @GremlinGroovy(value="true", frame=false)
    public boolean satisfies(FilterModel criteria);
    
    @JavaHandler
    public DetailsElement getDetails();  
    
    @JavaHandler
    public String getDetailsLinkLabel();
    
    public static abstract class Impl implements GraphMember, JavaHandlerContext<Vertex> {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
        
        public DetailsElement getDetails() {
            DetailsElement root = new DetailsElement();
            DetailsElement name = new DetailsElement("Name", this.getOrigin().getName());
            root.addSubElement(name);
            DetailsElement type = new DetailsElement("Type", this.getOrigin().getType() == null 
                    ? "" : this.getOrigin().getType().toString(true, true));
            root.addSubElement(type);
            if (this.getOrigin() instanceof Method) {
                addMethodSpecificDetails(root);
            }
//            if (this.getOrigin() instanceof Field) {
//                addFieldSpecificDetails(root);
//            }
            if (this.getProducedBean() != null) {
                root.addSubElement(new DetailsElement("Produced bean", this.getProducedBean()));
            } else {
                root.addSubElement(new DetailsElement("Produced bean", "<none>"));
            }
            List<GraphInjectionPoint> injectionPoints = 
                    Utils.iterableToList(this.getInjectionPoints());
            DetailsElement injectionPointsRoot = new DetailsElement("Injection points", 
                    injectionPoints.isEmpty() ? "<none>" : "");
            for (GraphInjectionPoint graphInjectionPoint : injectionPoints) {
                injectionPointsRoot.addSubElement(
                        DetailsElementFactory.create(graphInjectionPoint));
            }
            root.addSubElement(injectionPointsRoot);
            
            return root;
        }
        
//        private void addFieldSpecificDetails(DetailsElement root) {
//            Field field = (Field) this.getOrigin();
//            DetailsElement injectionPointRoot = new DetailsElement("Injection point", 
//                    field.getInjectionPoint() == null ? "<none>" : "");
//            if (field.getInjectionPoint() != null) {
//                DetailsElementFactory.addInjectioPointDetails(injectionPointRoot, 
//                        field.getInjectionPoint());
//            }
//            root.addSubElement(injectionPointRoot);
//        }

        private void addMethodSpecificDetails(DetailsElement root) {
            Method method = (Method) this.getOrigin();
            root.addSubElement(new DetailsElement("Is constructor", 
                    String.valueOf(method.isConstructor())));
            DetailsElement parameters = new DetailsElement("Method parameters", 
                    method.getParameters().isEmpty() ? "<none>" : "");
            for (MethodParameter param : method.getParameters()) {
                parameters.addSubElement(DetailsElementFactory.create(param));
            }
            root.addSubElement(parameters);
        }

        public String getDetailsLinkLabel() {
            return this.getOrigin().getDetailsLinkLabel();
        }
    }
}
