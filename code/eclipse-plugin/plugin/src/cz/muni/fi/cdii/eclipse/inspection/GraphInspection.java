package cz.muni.fi.cdii.eclipse.inspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

import cz.muni.fi.cdii.common.model.Bean;
import cz.muni.fi.cdii.common.model.Field;
import cz.muni.fi.cdii.common.model.InjectionPoint;
import cz.muni.fi.cdii.common.model.Member;
import cz.muni.fi.cdii.common.model.Method;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.common.model.Model;
import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Type;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphInjectionPoint;
import cz.muni.fi.cdii.eclipse.graph.model.GraphMember;
import cz.muni.fi.cdii.eclipse.graph.model.GraphType;

public class GraphInspection {

    private final Model model;
    
    private final InspectionTask task;
    
    /**
     * for filter part
     */
    private Set<String> packageNames = new TreeSet<>();
    /**
     * for filter part
     */
    private Set<Type> types = new HashSet<>();
    /**
     * for filter part
     */
    private Set<String> elNames = new TreeSet<>();
    /**
     * for filter part
     */
    private Set<Qualifier> qualifiers = new HashSet<>();
    
    private FramedGraph<Graph> framedGraph;
    
    private Map<Bean, GraphBean> beanCache = new HashMap<>();
    private Map<Type, GraphType> typeCache = new HashMap<>();
    private Map<InjectionPoint, GraphInjectionPoint> injectionPointCache = new HashMap<>();
    private Map<Member, GraphMember> memberCache = new HashMap<>();
    /**
     * This allows to break cyclic dependencies:
     * <br>
     * bean -> has Type -> has Member -> has InjectionPoint -> injects Bean
     * <br>
     * last part of the chain is left to the end of whole processing procedure
     */
    private Map<GraphInjectionPoint, Set<Bean>> deferredInjectedBeans = new HashMap<>();
    private Map<GraphMember, Bean> deferredProducedBeans = new HashMap<>();
    
    public GraphInspection (Inspection inspection) {
        this.model = inspection.getModel();
        this.task = inspection.getTask();
        
        createGraph();
        browseModel();
        processDeferred();
        cleanCreationalCaches();
    }

    private void processDeferred() {
        for (Map.Entry<GraphInjectionPoint, Set<Bean>> entry : 
            this.deferredInjectedBeans.entrySet()) {
            addBeansToInjectionPoint(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<GraphMember, Bean> entry : this.deferredProducedBeans.entrySet()) {
            addProducedBeanToMember(entry.getKey(), entry.getValue());
        }
    }

    private void addProducedBeanToMember(GraphMember graphMember, Bean bean) {
        GraphBean graphBean = this.addBean(bean);
        graphMember.setProducedBean(graphBean);
    }

    private void addBeansToInjectionPoint(GraphInjectionPoint graphInjectionPoint,
            Set<Bean> beans) {
        for (Bean bean : beans) {
            GraphBean graphBean = this.addBean(bean);
            graphInjectionPoint.addInjectedBean(graphBean);
        }
    }

    private void cleanCreationalCaches() {
        this.injectionPointCache = null;
        this.deferredInjectedBeans = null;
        this.deferredProducedBeans = null;
    }

    private void createGraph() {
        Graph tinkerGraph = new TinkerGraph();
        FramedGraphFactory factory = new FramedGraphFactory(
                new GremlinGroovyModule(), new JavaHandlerModule());
        this.framedGraph = factory.create(tinkerGraph);
    }

    private void browseModel() {
        for (Bean bean : this.model.getBeans()) {
            addBean(bean);
        }
    }

    private GraphBean addBean(Bean bean) {
        if (this.beanCache.containsKey(bean)) {
            return this.beanCache.get(bean);
        }
        if (bean.getElName() != null) {
            this.elNames.add(bean.getElName());
        }
        GraphType type = addType(bean.getType());
        Set<GraphType> typeSet = addTypes(bean.getTypeSet());
        List<String> qualifiers = qualifiersToList(bean.getQualifiers());
        GraphBean graphBean = this.framedGraph.addVertex(null, GraphBean.class);
        graphBean.setMainType(type);
        graphBean.setTypeSet(typeSet);
        graphBean.setElName(bean.getElName());
        graphBean.setOrigin(bean);
        graphBean.setScope(bean.getScope().toQualifiedString());
        graphBean.setQualifiers(qualifiers);
        this.beanCache.put(bean, graphBean);
        return graphBean;
    }

    private Set<GraphType> addTypes(Set<Type> typeSet) {
        HashSet<GraphType> result = new HashSet<>();
        for (Type type : typeSet) {
            GraphType graphType = addType(type);
            result.add(graphType);
        }
        return result;
    }

    private GraphType addType(Type type) {
        if (this.typeCache.containsKey(type)) {
            return this.typeCache.get(type);
        }
        this.packageNames.add(type.getPackage());
        this.types.add(type);
        Set<GraphMember> members = addMembers(type.getMembers());
        GraphType graphType = this.framedGraph.addVertex(null, GraphType.class);
        graphType.setName(type.toString(false, false));
        graphType.setCompleteName(type.toString(true, true));
        graphType.setPackage(type.getPackage());
        graphType.setOrigin(type);
        graphType.setMembers(members);
        this.typeCache.put(type, graphType);
        return graphType;
    }

    private Set<GraphMember> addMembers(Set<Member> members) {
        HashSet<GraphMember> result = new HashSet<>();
        for (Member member : members) {
            GraphMember graphMember = addMember(member);
            result.add(graphMember);
        }
        return result;
    }

    private GraphMember addMember(Member member) {
        if (this.memberCache.containsKey(member)) {
            return this.memberCache.get(member);
        }
        GraphMember graphMember = this.framedGraph.addVertex(null, GraphMember.class);
        Bean producedBean = member.getProducedBean();
        Set<GraphInjectionPoint> graphInjectionPoints = addInjectionPoints(member);
        graphMember.setOrigin(member);
        if (producedBean != null) {
            this.deferredProducedBeans.put(graphMember, producedBean);
        }
        graphMember.setInjectionPoints(graphInjectionPoints);
        this.memberCache.put(member, graphMember);
        return graphMember;
    }

    private Set<GraphInjectionPoint> addInjectionPoints(Member member) {
        if (member instanceof Field) {
            final Field field = (Field) member;
            if (field.getInjectionPoint() == null) {
                return Collections.emptySet();
            }
            return Collections.singleton(addInjectionPoint(field.getInjectionPoint()));
        }
        if (member instanceof Method) {
            final Method method = (Method) member;
            Set<GraphInjectionPoint> result = new HashSet<>();
            for (MethodParameter paremeter : method.getParameters()) {
                InjectionPoint injectionPoint = paremeter.getInjectionPoint();
                if (injectionPoint != null) {
                    GraphInjectionPoint graphInjectionPoint = addInjectionPoint(injectionPoint);
                    result.add(graphInjectionPoint);
                }
            }
            return result;
        }
        throw new RuntimeException("Unexpected type of member class.");
    }

    private GraphInjectionPoint addInjectionPoint(InjectionPoint injectionPoint) {
        if (this.injectionPointCache.containsKey(injectionPoint)) {
            return this.injectionPointCache.get(injectionPoint);
        }
        if (injectionPoint.getElName() != null) {
            this.elNames.add(injectionPoint.getElName());
        }
        this.qualifiers.addAll(injectionPoint.getQualifiers());
        List<String> stringQualifiers = qualifiersToList(injectionPoint.getQualifiers());
        Set<Bean> resolvedBeans = injectionPoint.getResolvedBeans();
        GraphInjectionPoint graphInjectionPoint = this.framedGraph.addVertex(null, GraphInjectionPoint.class);
        graphInjectionPoint.setOrigin(injectionPoint);
        graphInjectionPoint.setQualifiers(stringQualifiers);
        this.injectionPointCache.put(injectionPoint, graphInjectionPoint);
        this.deferredInjectedBeans.put(graphInjectionPoint, resolvedBeans);
        return graphInjectionPoint;
    }

    private List<String> qualifiersToList(Set<Qualifier> qualifiers) {
        List<String> result = new ArrayList<>();
        for (Qualifier qualifier : qualifiers) {
            String stringQualifier = qualifier.toString();
            result.add(stringQualifier);
        }
        return result;
    }

    public FramedGraph<Graph> getFramedGraph() {
        return framedGraph;
    }

    public InspectionTask getTask() {
        return task;
    }

    public Map<Bean, GraphBean> getBeanMap() {
        return beanCache;
    }

    public Map<Type, GraphType> getTypeMap() {
        return typeCache;
    }

    public Map<Member, GraphMember> getMemberMap() {
        return memberCache;
    }

    public Set<String> getPackageNames() {
        return packageNames;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public Set<String> getElNames() {
        return elNames;
    }

    public Set<Qualifier> getQualifiers() {
        return qualifiers;
    }

}
