package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import org.eclipse.jface.viewers.StyledString;

public class ElementFilterModel implements FilterPartModel {
    
    private boolean isSelected = false;
    private final String label;
    private final StyledString fancyLabel;
    private final String queryKey;
    private CategoryFilterModel parent;
    
    public ElementFilterModel(String label, String queryKey) {
        this.label = label;
        this.fancyLabel = null;
        this.queryKey = queryKey;
    }
    
    public ElementFilterModel(StyledString fancyLabel, String queryKey) {
        this.label = null;
        this.fancyLabel = fancyLabel;
        this.queryKey = queryKey;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getLabel() {
        return label;
    }

    public StyledString getFancyLabel() {
        return fancyLabel;
    }

    public String getQueryKey() {
        return queryKey;
    }
    
    public CategoryFilterModel getParent() {
        return parent;
    }

    public void setParent(CategoryFilterModel parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return (this.fancyLabel != null ? this.fancyLabel.getString() : this.label) + " | " 
                + this.queryKey; 
    }
}
