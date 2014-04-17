package cz.muni.fi.cdii.common.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class for details part.
 */
public class DetailsElement {

    final private String label;
    final private Object value;
    private List<DetailsElement> children = new ArrayList<>();
    private DetailsElement parent;

    /**
     * This constructor is intended for root of details structure only.
     */
    public DetailsElement() {
        this.label = null;
        this.value = null;
    }
    
    public DetailsElement(String label, String value) {
        super();
        this.label = label;
        this.value = value;
    }
    
    public DetailsElement(String label, Viewable value) {
        super();
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
    
    public DetailsElement getParent() {
        return parent;
    }

    public void setParent(DetailsElement parent) {
        this.parent = parent;
    }

    public void addSubElement(DetailsElement child) {
        child.setParent(this);
        this.children.add(child);
    }

    public List<DetailsElement> getChildren() {
        return Collections.unmodifiableList(this.children);
    }
    
    @Override
    public String toString() {
        return this.getLabel() + ": " + this.getValue() + " " 
                + "(kids: " + this.children.size() + ")";
    }
    
}
