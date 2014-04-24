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

import cz.muni.fi.cdii.eclipse.CdiiEventTopics;
import cz.muni.fi.cdii.eclipse.graph.model.GraphElement;

public class ValueColumnLabelProvider extends CellLabelProvider {
    
    private EventBroker broker;
    private Map<GraphElement, Button> buttons = new HashMap<>();

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
            if (value instanceof GraphElement) {
                GraphElement graphElement = (GraphElement) value;
                setShowInGraphButtonEditor(cell, graphElement);
                return;
            }
        }
    }

    private void setShowInGraphButtonEditor(ViewerCell cell, final GraphElement graphElement) {
        Button button;
        if (this.buttons.containsKey(graphElement)) {
            button = this.buttons.get(graphElement);
        } else {
            button = createButton(cell, graphElement);
        }

        TreeItem item = (TreeItem) cell.getItem();
        TreeEditor editor = new TreeEditor(item.getParent());
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(button, item, cell.getColumnIndex());
    }

    private Button createButton(ViewerCell cell, final GraphElement graphElement) {
        Button button;
        button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
        String buttonText = graphElement.getDetailsLinkLabel();
        button.setText(buttonText);
        button.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                broker.post(CdiiEventTopics.SELECT_NODE, graphElement);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        this.buttons.put(graphElement, button);
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
