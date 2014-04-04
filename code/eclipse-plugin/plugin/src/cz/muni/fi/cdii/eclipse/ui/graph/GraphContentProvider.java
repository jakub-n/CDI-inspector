package cz.muni.fi.cdii.eclipse.ui.graph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.INestedContentProvider;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import cz.muni.fi.cdii.eclipse.graph.model.Constants;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphMember;
import cz.muni.fi.cdii.eclipse.graph.model.GraphType;

public class GraphContentProvider implements IGraphEntityContentProvider, INestedContentProvider {

    private FramedGraph<Graph> input;

    public GraphContentProvider() {
    }

    @Override
    public void dispose() {
        // nothing
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof FramedGraph) {
            @SuppressWarnings("unchecked")
            FramedGraph<Graph> graph = (FramedGraph<Graph>) newInput;
            this.input = graph;
            return;
        }
        if (newInput == null) {
            this.input = null;
            return;
        }
        throw new RuntimeException("Unexpected input class: "
                + (newInput == null ? "null" : newInput.getClass()));
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
            List<GraphMember> children = iterableToList(typeVertex.getMembers());
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
        List<GraphBean> beans = iterableToList(this.input.getVertices(
                Constants.VERTEX_TYPE_PROPERTY, GraphBean.VERTEX_TYPE_NAME, GraphBean.class));
        List<GraphType> types = getMainTypeNodes(); 
        ArrayList<Object> result = new ArrayList<>();
        result.addAll(beans);
        result.addAll(types);
        return result.toArray();
    }
    
    public double getWeight(Object entity1, Object entity2) {
        // TODO edit
        System.out.println("weight called");
        return 0;
    }

    private List<GraphType> getMainTypeNodes() {
        GremlinPipeline<Vertex, Vertex> mainTypesPipeline = new GremlinPipeline<Vertex, Vertex>()
                .has(Constants.VERTEX_TYPE_PROPERTY, GraphBean.VERTEX_TYPE_NAME).out("mainType");
        mainTypesPipeline.setStarts(this.input.getVertices());
        List<GraphType> types = iterableToList(
                this.input.frameVertices(mainTypesPipeline, GraphType.class));
        return types;
    }

    @Override
    public Object[] getConnectedTo(Object entity) {
        if (entity instanceof GraphBean) {
            GraphBean bean = (GraphBean) entity;
            GraphType type = bean.getMainType();
            List<GraphMember> injectionTargetMembers = iterableToList(bean
                    .getInjectionTargetMembers());
            ArrayList<Object> result = new ArrayList<>();
            result.add(type);
            result.addAll(injectionTargetMembers);
            return result.toArray();
        }
        if (entity instanceof GraphType) {
            return new Object[0];
        }
        if (entity instanceof GraphMember) {
            GraphMember member = (GraphMember) entity;
            GraphBean producedBean = member.getProducedBean();
            if (producedBean != null) {
                return new Object[] { producedBean };
            } else {
                return new Object[0];
            }
        }
        throw new RuntimeException("Unexpected object class: "
                + (entity == null ? "null" : entity.getClass()));
    }

    private static <T> List<T> iterableToList(Iterable<T> input) {
        ArrayList<T> result = new ArrayList<>();
        for (T item : input) {
            result.add(item);
        }
        return result;
    }

}
