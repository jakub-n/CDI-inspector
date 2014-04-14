package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryFilterModel implements FilterPartModel {
    
    private final String name;
    private final List<ElementFilterModel> children = new ArrayList<>();
    
    public CategoryFilterModel(String name) {
        this.name = name;
    }
    
    public void addElement(ElementFilterModel element) {
        element.setParent(this);
        this.children.add(element);
    }
    
    public List<ElementFilterModel> getElements() {
        return Collections.unmodifiableList(this.children);
    }

    public String getName() {
        return name;
    }
}
