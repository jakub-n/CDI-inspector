package cz.muni.fi.cdii.eclipse.graph.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Type;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElement;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElementFactory;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterModel;

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
    
    /**
     * Result has zero or one items.
     * @return types that has members that produces this bean
     */
    @GremlinGroovy("it.in('produces').in('hasMember').dedup()")
    public Iterable<GraphType> getProducingType();
    
    @GremlinGroovy("it.in('produces').dedup()")
    public Iterable<GraphMember> getProducingMembers();
    

    @JavaHandler
    public boolean satisfies(FilterModel criteria);
    
    @JavaHandler
    public String getTooltipText();
    
    @JavaHandler
    public DetailsElement getDetails();
    
    @JavaHandler
    public String getDetailsLinkLabel();
    
    public static abstract class Impl implements JavaHandlerContext<Vertex>, GraphBean {
        
        @Initializer
        public void init() {
            it().setProperty(Constants.VERTEX_TYPE_PROPERTY, VERTEX_TYPE_NAME);
        }
        
        public String getTooltipText() {
            return this.getOrigin().getType().toString(true, true);
        }
        
        public DetailsElement getDetails() {
            DetailsElement root = new DetailsElement();
            root.addSubElement(new DetailsElement("Type", this.getMainType()));
            root.addSubElement(new DetailsElement("EL name", 
                    this.getOrigin().getElName() != null 
                            ? this.getOrigin().getElName() 
                            : "<none>"));
            root.addSubElement(DetailsElementFactory.create(this.getOrigin().getScope()));
            root.addSubElement(DetailsElementFactory.create(this.getOrigin().getQualifiers()));
            List<GraphMember> producingMembers = Utils.iterableToList(this.getProducingMembers());
            if (producingMembers.isEmpty()) {
                root.addSubElement(new DetailsElement("Produced by", "<none>"));
            } else {
                DetailsElement producedByRoot = new DetailsElement("Produced by","");
                for (GraphMember producingMember : producingMembers) {
                    producedByRoot.addSubElement(new DetailsElement("", producingMember));
                }
                root.addSubElement(producedByRoot);
            }            
            List<GraphMember> injectionTargetMembers = Utils.iterableToList(
                    this.getInjectionTargetMembers());
            if (injectionTargetMembers.isEmpty()) {
                root.addSubElement(new DetailsElement("Injected into", "<none>"));
            } else {
                DetailsElement producedByRoot = new DetailsElement("Injected into","");
                for (GraphMember injectionTargetMember : injectionTargetMembers) {
                    producedByRoot.addSubElement(new DetailsElement("", injectionTargetMember));
                }
                root.addSubElement(producedByRoot);
            }
            DetailsElement typeSet = new DetailsElement("Type set", "");
            for (Type type : this.getOrigin().getTypeSet()) {
                typeSet.addSubElement(new DetailsElement("", type.toString(true, true)));
            }
            root.addSubElement(typeSet);
            return root;
        }
        
        public String getDetailsLinkLabel() {
            return "@" + this.getOrigin().getScope().getName() 
                    + " " + this.getOrigin().getType().toString(false, true);
        }
        
        public boolean satisfies(FilterModel criteria) {
            boolean typeNameSatisfied = criteria.getClassName() == "" ? true : 
                getMainType().getOrigin().toString(true, false).contains(criteria.getClassName());
            boolean elNameSatisfied = criteria.getElNames().isEmpty() ? true : 
                criteria.getElNames().contains(this.getElName());
            boolean packageSatisfied = criteria.getPackages().isEmpty() ? true : 
                criteria.getPackages().contains(this.getMainType().getPackage());
            boolean typesSatisfied = criteria.getTypes().isEmpty() ? true : 
                hasIntersection(typesToFullParameterizedNames(this.getOrigin().getTypeSet()), 
                        criteria.getTypes());
            boolean qualifiersSatisfied = criteria.getQualifiers().isEmpty() ? true : 
                hasIntersection(qualifiersToQualifiedNames(this.getOrigin().getQualifiers()), 
                        criteria.getQualifiers());
            boolean result = typeNameSatisfied && elNameSatisfied && packageSatisfied 
                    && typesSatisfied && qualifiersSatisfied;
            return result;
        }
        
        private static Set<String> qualifiersToQualifiedNames(Set<Qualifier> qualifiers) {
            HashSet<String> result = new HashSet<>();
            for (Qualifier qualifier : qualifiers) {
                String name = qualifier.toString(true);
                result.add(name);
            }
            return result;
        }

        private static <T> boolean hasIntersection(Set<T> editableSet, Set<T> anotherSet) {
            editableSet.retainAll(anotherSet);
            boolean isIntersectionNonEmpty = !editableSet.isEmpty();
            return isIntersectionNonEmpty;
        }
        
        private static Set<String> typesToFullParameterizedNames(Set<Type> types) {
            HashSet<String> result = new HashSet<>();
            for (Type type : types) {
                String name = type.toString(true, true);
                result.add(name);
            }
            return result;
        }
    }
}
