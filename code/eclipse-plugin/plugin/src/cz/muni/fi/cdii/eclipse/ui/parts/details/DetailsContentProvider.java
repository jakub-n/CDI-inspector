package cz.muni.fi.cdii.eclipse.ui.parts.details;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cz.muni.fi.cdii.common.model.DetailsElement;

public class DetailsContentProvider implements ITreeContentProvider {
    
    private DetailsElement input;

    @Override
    public void dispose() {
        // do nothing
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput instanceof DetailsElement) {
            this.input = (DetailsElement) newInput;
            return;
        }
        if (newInput == null) {
            this.input = null;
            return;
        }
        throw new RuntimeException("Unexpected input type.");
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof DetailsElement) {
            return this.getChildren(inputElement);
        }
        throw new RuntimeException("Unexpected element type.");
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

}
