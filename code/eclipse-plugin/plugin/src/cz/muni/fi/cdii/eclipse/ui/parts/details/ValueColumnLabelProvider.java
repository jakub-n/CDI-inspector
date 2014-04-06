package cz.muni.fi.cdii.eclipse.ui.parts.details;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import cz.muni.fi.cdii.common.model.DetailsElement;
import cz.muni.fi.cdii.common.model.Viewable;
import cz.muni.fi.cdii.eclipse.CdiiEventTopics;

public class ValueColumnLabelProvider extends CellLabelProvider {
    
    private EventBroker broker;
    private Map<Viewable, Button> buttons = new HashMap<>();

    public ValueColumnLabelProvider(EventBroker broker) {
        super();
        this.broker = broker;
    }

    @Override
    public void update(ViewerCell cell) {
        if (cell.getElement() instanceof DetailsElement) {
            DetailsElement element = (DetailsElement) cell.getElement();
            Object value = element.getValue();
            if (value instanceof String) {
                String text = (String) element.getValue() ;
                cell.setText(text);
                return;
            }
            if (value instanceof Viewable) {
                Viewable viewable = (Viewable) value;
                setShowInGraphButtonEditor(cell, viewable);
                return;
            }
        }
    }

    private void setShowInGraphButtonEditor(ViewerCell cell, final Viewable viewable) {
        Button button;
        if (this.buttons.containsKey(viewable)) {
            button = this.buttons.get(viewable);
        } else {
            button = createButton(cell, viewable);
        }

        TreeItem item = (TreeItem) cell.getItem();
        TreeEditor editor = new TreeEditor(item.getParent());
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(button, item, cell.getColumnIndex());
    }

    private Button createButton(ViewerCell cell, final Viewable viewable) {
        Button button;
        button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
        button.setText("Show in graph");
        button.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                broker.post(CdiiEventTopics.SELECT_NODE, viewable);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        this.buttons.put(viewable, button);
        return button;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        this.disposeWidgets();
    }
    
    public void disposeWidgets() {
        for (Widget widget : this.buttons.values()) {
            widget.dispose();
        }
        this.buttons = new HashMap<>();
    }

}
