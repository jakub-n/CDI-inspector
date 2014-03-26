package cz.muni.fi.cdii.eclipse.ui.detailview;

import java.util.List;

public class Property {

    private String key;
    private String value;
    private List<Property> children;
    private List<Action> actions;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public List<Property> getChildren() {
        return children;
    }
    public void setChildren(List<Property> children) {
        this.children = children;
    }
    public List<Action> getActions() {
        return actions;
    }
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
    
    
}
