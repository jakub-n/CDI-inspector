package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import cz.muni.fi.cdii.eclipse.ui.parts.FilterPart;

/**
 * Label provider for check-boxes in filter part
 */
public class CheckboxColumnLabelProvider extends CellLabelProvider {
    
    private Map<FilterPartModel, Button> checkboxes = new HashMap<>();
    
    private final FilterPart filterPart;
    
    public CheckboxColumnLabelProvider(FilterPart filterPart) {
        super();
        this.filterPart = filterPart;
    }

    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        if (element instanceof FilterPartModel) {
            FilterPartModel modelElement = (FilterPartModel) element;
            Button checkbox = getCheckbox(modelElement, cell);
            TreeItem item = (TreeItem) cell.getItem();
            TreeEditor treeEditor = new TreeEditor(item.getParent());
            treeEditor.grabHorizontal = true;
            treeEditor.grabVertical = true;
            treeEditor.setEditor(checkbox, item, cell.getColumnIndex());
        }
    }
    
    private Button getCheckbox(FilterPartModel modelElement, ViewerCell cell) {
        if (this.checkboxes.containsKey(modelElement)) {
            return this.checkboxes.get(modelElement);
        }
        Button result = new Button((Composite) cell.getViewerRow().getControl(), SWT.CHECK);
        this.checkboxes.put(modelElement, result);
        attachSelectionListener(result, modelElement);
        return result;
    }

    private void attachSelectionListener(final Button checkbox, 
            final FilterPartModel modelElement) {
        if (modelElement instanceof CategoryFilterModel) {
            checkbox.addSelectionListener(new SelectionListener() {
                
                /**
                 * Sets all checkboxes in category to state of this checkbox.
                 */
                @Override
                public void widgetSelected(SelectionEvent e) {
                    boolean categorySelectionState = checkbox.getSelection();
                    CategoryFilterModel category = (CategoryFilterModel) modelElement;
                    for (ElementFilterModel element : category.getElements()) {
                        Button elemCheckbox = 
                                CheckboxColumnLabelProvider.this.checkboxes.get(element);
                        elemCheckbox.setSelection(categorySelectionState);
                    }
                    CheckboxColumnLabelProvider.this.filterPart.doFilter();
                }
                
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
            return;
        }
        if (modelElement instanceof ElementFilterModel) {
            checkbox.addSelectionListener(new SelectionListener() {
                
                @Override
                public void widgetSelected(SelectionEvent e) {
                    CheckboxColumnLabelProvider.this.filterPart.doFilter();
                }
                
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.disposeWidgets();
    }
    
    public void disposeWidgets() {
        for (Button checkbox : this.checkboxes.values()) {
            checkbox.dispose();
        }
        this.checkboxes = new HashMap<>();
    }
    
    /**
     * Unchecks all checkboxes;
     */
    public void reset() {
        for (Button checkbox : this.checkboxes.values()) {
            checkbox.setSelection(false);
        }
    }

    /**
     * @return set of query keys of checked elements in given category
     */
    public Set<String> getFilterSetForCategory(CategoryFilterModel category) {
        Set<String> result = new HashSet<>();
        for (ElementFilterModel element : category.getElements()) {
            boolean checked = this.checkboxes.get(element).getSelection();
            if (checked) {
                result.add(element.getQueryKey());
            }
        }
        return result;
    }

}
