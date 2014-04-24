package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.INestedContentProvider;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import cz.muni.fi.cdii.eclipse.CdiiEventTopics;
import cz.muni.fi.cdii.eclipse.graph.model.Constants;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphElement;
import cz.muni.fi.cdii.eclipse.graph.model.GraphMember;
import cz.muni.fi.cdii.eclipse.graph.model.GraphType;
import cz.muni.fi.cdii.eclipse.graph.model.Utils;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterModel;

public class GraphContentProvider implements IGraphEntityContentProvider, INestedContentProvider, 
        EventHandler {

    private IEventBroker broker;
    private CdiiGraphViewer graphViewer;
    private FramedGraph<Graph> input;
    private FilterModel filterCriteria;
    /**
     * all currently visible (~ shown) nodes
     */
    private Set<GraphElement> filterSet;

    public GraphContentProvider(IEventBroker broker, CdiiGraphViewer graphViewer) {
        this.broker = broker;
        this.graphViewer = graphViewer;
        this.filterCriteria = null;
        this.broker.subscribe(CdiiEventTopics.FILTER_GRAPH, this);
    }
    

    public Set<GraphElement> getFilterSet() {
        return filterSet;
    }


    @Override
    public void dispose() {
        this.broker.unsubscribe(this);
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof FramedGraph) {
            @SuppressWarnings("unchecked")
            FramedGraph<Graph> graph = (FramedGraph<Graph>) newInput;
            this.input = graph;
            this.updateFilterSet();
            return;
        }
        if (newInput == null) {
            this.input = null;
            return;
        }
        throw new RuntimeException("Unexpected input class: "
                + (newInput == null ? "null" : newInput.getClass()));
    }

    private void updateFilterSet() {
        List<GraphBean> allBeans = Utils.iterableToList(this.input.getVertices(
                Constants.VERTEX_TYPE_PROPERTY, GraphBean.VERTEX_TYPE_NAME, GraphBean.class));
        Set<GraphBean> filteredBeans = filterBeans(allBeans);
        Set<GraphElement> filterSet = new HashSet<>();
        filterSet.addAll(filteredBeans);
        for (GraphBean filteredBean : filteredBeans) {
            Collection<? extends GraphElement> adjacentTypes = getAdjacentTypes(filteredBean);
            filterSet.addAll(adjacentTypes);
        }
        List<GraphMember> allMembers = Utils.iterableToList(
                this.input.getVertices(Constants.VERTEX_TYPE_PROPERTY, GraphMember.VERTEX_TYPE_NAME,
                        GraphMember.class));
        filterSet.addAll(allMembers);
        this.filterSet = Collections.unmodifiableSet(filterSet);
        updateGraphCanvasSize();
    }

    private void updateGraphCanvasSize() {
        int numOfTopLevelNodes = this.getElements(this.input).length;
        int canvasXPreferedLength = 65 * numOfTopLevelNodes;
        int canvasYPreferedLength = 100 * ((int) Math.ceil(Math.log(numOfTopLevelNodes)));
        Point controlSize = this.graphViewer.getGraphControl().getSize();
        int canvasX = Math.max(controlSize.x, canvasXPreferedLength);
        int canvasY = Math.max(controlSize.y, canvasYPreferedLength);
        this.graphViewer.getGraphControl().setPreferredSize(canvasX, canvasY);
    }

    private Set<GraphBean> filterBeans(List<GraphBean> allBeans) {
        Set<GraphBean> result = new HashSet<>();
        for (GraphBean bean : allBeans) {
            if (this.filterCriteria == null || bean.satisfies(this.filterCriteria)) {
                result.add(bean);
            }
        }
        return result;
    }

    private Collection<? extends GraphElement> getAdjacentTypes(GraphBean bean) {
        Set<GraphElement> result = new HashSet<>();
        GraphType mainType = bean.getMainType();
        List<GraphType> injectionTargets = Utils.iterableToList(bean.getAuxiliaryInjectionTargetTypes());
        List<GraphType> producer = Utils.iterableToList(bean.getProducingType());
        result.add(mainType);
        result.addAll(injectionTargets);
        result.addAll(producer);
        return result;
    }

    @Override
    public boolean hasChildren(Object element) {
        boolean hasChildren = element instanceof GraphType;
        return hasChildren;
    }

    @Override
    public Object[] getChildren(Object element) {
        if (element instanceof GraphType) {
            GraphType typeVertex = (GraphType) element;
            List<GraphMember> children = Utils.iterableToList(typeVertex.getMembers());
            return children.toArray();
        }
        throw new RuntimeException("Unexpected element type: "
                + (element == null ? "null" : element.getClass()));
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (this.input == null) {
            return new Object[0];
        }
        List<GraphBean> beans = Utils.iterableToList(this.input.getVertices(
                Constants.VERTEX_TYPE_PROPERTY, GraphBean.VERTEX_TYPE_NAME, GraphBean.class));
        List<GraphType> types = getMainTypeNodes(); 
        ArrayList<GraphElement> allElements = new ArrayList<>();
        allElements.addAll(beans);
        allElements.addAll(types);
        Object[] filteredElements = filter(allElements);
        return filteredElements;
    }

    private List<GraphType> getMainTypeNodes() {
        GremlinPipeline<Vertex, Vertex> mainTypesPipeline = new GremlinPipeline<Vertex, Vertex>()
                .has(Constants.VERTEX_TYPE_PROPERTY, GraphBean.VERTEX_TYPE_NAME).out("mainType");
        mainTypesPipeline.setStarts(this.input.getVertices());
        List<GraphType> types = Utils.iterableToList(
                this.input.frameVertices(mainTypesPipeline, GraphType.class));
        return types;
    }

    @Override
    public Object[] getConnectedTo(Object entity) {
        if (entity instanceof GraphBean) {
            GraphBean bean = (GraphBean) entity;
            GraphType type = bean.getMainType();
            List<GraphMember> injectionTargetMembers = Utils.iterableToList(bean
                    .getInjectionTargetMembers());
            List<GraphType> auxInjectionTargetTypes = Utils.iterableToList(
                    bean.getAuxiliaryInjectionTargetTypes());
            ArrayList<GraphElement> elements = new ArrayList<>();
            elements.add(type);
            elements.addAll(injectionTargetMembers);
            elements.addAll(auxInjectionTargetTypes);
            Object[] filteredElements = filter(elements);
            return filteredElements;
        }
        if (entity instanceof GraphType) {
            GraphType graphType = (GraphType) entity;
            List<GraphBean> auxProcudesTragets = Utils.iterableToList(
                    graphType.getAuxiliaryProducesTargets());
            Object[] filteredElements = filter(auxProcudesTragets);
            return filteredElements;
        }
        if (entity instanceof GraphMember) {
            GraphMember member = (GraphMember) entity;
            GraphBean producedBean = member.getProducedBean();
            if (producedBean != null) {
                return filter(Collections.singletonList(producedBean));
            } else {
                return new Object[0];
            }
        }
        throw new RuntimeException("Unexpected object class: "
                + (entity == null ? "null" : entity.getClass()));
    }

    private Object[] filter(Collection<? extends GraphElement> elements) {
        Set<GraphElement> elementsSet = new HashSet<>(elements);
        elementsSet.retainAll(this.filterSet);
        Object[] result = elementsSet.toArray();
        return result;
    }

    /**
     * {@link CdiiEventTopics#FILTER_GRAPH}
     */
    @Override
    public void handleEvent(Event event) {
        String topic = event.getTopic();
        if (CdiiEventTopics.FILTER_GRAPH.equals(topic)) {
            FilterModel criteria = (FilterModel) event.getProperty(IEventBroker.DATA);
            filter(criteria);
            return;
        }
    }


    /**
     * Filter graph and refresh it. 
     * @param criteria all bean nodes have to satisfy in order to be show. 
     * null means show everything
     */
    public void filter(FilterModel criteria) {
        this.filterCriteria = criteria;
        this.updateFilterSet();
        this.graphViewer.refresh(false);
        this.graphViewer.applyLayout();
    }

}
