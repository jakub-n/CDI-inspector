package cz.muni.fi.cdii.eclipse.ui.parts.details;

import java.util.List;
import java.util.Set;

import cz.muni.fi.cdii.common.model.InjectionPoint;
import cz.muni.fi.cdii.common.model.MethodParameter;
import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Scope;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.graph.model.GraphInjectionPoint;
import cz.muni.fi.cdii.eclipse.graph.model.Utils;

public class DetailsElementFactory {

    private DetailsElementFactory() {}
    
    public static DetailsElement create(Scope scope) {
        DetailsElement root = new DetailsElement("Scope", "");
        root.addSubElement(new DetailsElement("Name", scope.getName()));
        root.addSubElement(new DetailsElement("Package", scope.getPackage()));
        root.addSubElement(new DetailsElement("Is pseudo", scope.isPseudo() ? "true" : "false"));
        return root;
    }

    public static DetailsElement create(Set<Qualifier> qualifiers) {
        DetailsElement root = new DetailsElement("Qualifiers", 
                qualifiers.isEmpty() ? "<none>" : "");
        for (Qualifier qualifier : qualifiers) {
            root.addSubElement(DetailsElementFactory.create(qualifier));
        }
        return root;
    }
    
    private static DetailsElement create(Qualifier qualifier) {
        DetailsElement root = new DetailsElement(qualifier.toString(false), 
                qualifier.getType().getPackage());
        return root;
    }

    public static DetailsElement create(GraphInjectionPoint graphInjectionPoint) {
        InjectionPoint origin = graphInjectionPoint.getOrigin();
        DetailsElement root = new DetailsElement(origin.getType().toString(false, true), 
                origin.getType().getPackage());
        root.addSubElement(new DetailsElement("EL name", 
                origin.getElName()==null ? "<none>" : origin.getElName()));
        DetailsElement qualifiersRoot = new DetailsElement("Qualifiers", 
                origin.getQualifiers().isEmpty() ? "<none>" : "");
        for (Qualifier qualifier : origin.getQualifiers()) {
            qualifiersRoot.addSubElement(DetailsElementFactory.create(qualifier));
        }
        root.addSubElement(qualifiersRoot);
        List<GraphBean> injectedBeans = Utils.iterableToList(
                graphInjectionPoint.getInjectedBeans());
        for (GraphBean graphBean : injectedBeans) {
            root.addSubElement(new DetailsElement("Resolved bean", graphBean));
        }
        return root;
    }

    public static DetailsElement create(MethodParameter param) {
        DetailsElement root = new DetailsElement("", param.getType().toString(true, true));
        return root;
    }
}
