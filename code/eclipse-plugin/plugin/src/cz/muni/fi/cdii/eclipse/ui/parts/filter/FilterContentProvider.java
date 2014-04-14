package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FilterContentProvider implements ITreeContentProvider {

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    /**
     * expects {@link CategoryFilterModel}{@code []} as argument
     */
    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement != null 
                && inputElement.getClass().isArray() 
                && inputElement.getClass().getComponentType().isAssignableFrom(
                        CategoryFilterModel.class)) {
            return (Object[]) inputElement;
        }
        return new Object[0];
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof CategoryFilterModel) {
            CategoryFilterModel category = (CategoryFilterModel) parentElement;
            return category.getElements().toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof ElementFilterModel) {
            ElementFilterModel filterElement = (ElementFilterModel) element;
            return filterElement.getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof CategoryFilterModel) {
            return true;
        }
        return false;
    }

}
