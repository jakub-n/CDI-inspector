package cz.muni.fi.cdii.eclipse.ui.parts.details;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class LabelColumnLabelProvider extends CellLabelProvider {
    
    @Override
    public void update(ViewerCell cell) {
        if (cell.getElement() instanceof DetailsElement) {
            DetailsElement element = (DetailsElement) cell.getElement();
            String text = element.getLabel();
            cell.setText(text);
        }
    }

}
