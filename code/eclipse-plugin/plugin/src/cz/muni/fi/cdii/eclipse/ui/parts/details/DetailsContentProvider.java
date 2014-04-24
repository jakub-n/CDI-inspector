package cz.muni.fi.cdii.eclipse.ui.parts.details;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DetailsContentProvider implements ITreeContentProvider {
    
    @Override
    public void dispose() {
        // do nothing
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof DetailsElement) {
            return this.getChildren(inputElement);
        }
        return new Object[0];
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof DetailsElement) {
            DetailsElement parent = (DetailsElement) parentElement;
            return parent.getChildren().toArray();
        }
        throw new RuntimeException("Unexpected parent type.");
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof DetailsElement) {
            DetailsElement detailsElement = (DetailsElement) element;
            return detailsElement.getParent();
        }
        throw new RuntimeException("Unexpected element type.");
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof DetailsElement) {
            DetailsElement detailsElement = (DetailsElement) element;
            return !detailsElement.getChildren().isEmpty();
        }
        throw new RuntimeException("Unexpected element type.");
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

}
