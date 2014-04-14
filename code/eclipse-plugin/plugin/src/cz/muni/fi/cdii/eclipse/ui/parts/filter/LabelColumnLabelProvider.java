package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Label provider for labels in filter part 
 */
public class LabelColumnLabelProvider extends StyledCellLabelProvider {

    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        if (element instanceof CategoryFilterModel) {
            CategoryFilterModel category = (CategoryFilterModel) element;
            String text = category.getName();
            cell.setText(text);
            return;
        }
        if (element instanceof ElementFilterModel) {
            ElementFilterModel filterElement = (ElementFilterModel) element;
            if (filterElement.getFancyLabel() != null) {
                cell.setText(filterElement.getFancyLabel().getString());
                cell.setStyleRanges(filterElement.getFancyLabel().getStyleRanges());
            } else {
                cell.setText(filterElement.getLabel());
            }
            return;
        }
    }

}
