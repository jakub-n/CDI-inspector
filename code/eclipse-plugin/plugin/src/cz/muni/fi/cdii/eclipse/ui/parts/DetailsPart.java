package cz.muni.fi.cdii.eclipse.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import cz.muni.fi.cdii.common.model.DetailsElement;
import cz.muni.fi.cdii.eclipse.CdiiEventTopics;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsContentProvider;
import cz.muni.fi.cdii.eclipse.ui.parts.details.LabelColumnLabelProvider;
import cz.muni.fi.cdii.eclipse.ui.parts.details.ValueColumnLabelProvider;

public class DetailsPart implements EventHandler {
    
    public static final String PART_ID = "cz.muni.fi.cdii.eclipse.plugin.partdescriptor.details";
    
    private TreeViewer treeViewer;
    
    @Inject
    private EventBroker broker;

    private ValueColumnLabelProvider valueLabelProvider;
    
    @PostConstruct
    public void createControls(Composite parent, @Optional InspectorPart inspectorPart) {
        this.broker.subscribe(CdiiEventTopics.UPDATE_DETAILS, this);
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
        this.treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        this.treeViewer.getTree().setHeaderVisible(true);
        this.treeViewer.getTree().setLinesVisible(true);
        TreeViewerColumn labelColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
        labelColumn.setLabelProvider(new LabelColumnLabelProvider());
        labelColumn.getColumn().setWidth(200);
        labelColumn.getColumn().setMoveable(true);
        labelColumn.getColumn().setText("Property");
        TreeViewerColumn valueColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
        valueLabelProvider = new ValueColumnLabelProvider(this.broker);
        valueColumn.setLabelProvider(valueLabelProvider);
        valueColumn.getColumn().setWidth(200);
        valueColumn.getColumn().setMoveable(true);
        valueColumn.getColumn().setText("Value");
        this.treeViewer.setContentProvider(new DetailsContentProvider());
        broker.post(CdiiEventTopics.UPDATE_DETAILS_REQUEST, null);
    }
    
    @Focus
    public void onFocus() {
        this.treeViewer.getTree().setFocus();
    }

    /**
     * {@link CdiiEventTopics#UPDATE_DETAILS}
     */
    @Override
    public void handleEvent(Event event) {
        /*
         * sometimes an event come even if the part is already disposed, maybe an E4 bug
         */
        if (this.treeViewer.getTree().isDisposed()) {
            this.broker.unsubscribe(this);
            return;
        }
        
        this.valueLabelProvider.disposeWidgets();
        DetailsElement input = (DetailsElement) event.getProperty(IEventBroker.DATA);
        this.treeViewer.setInput(input);
        this.treeViewer.expandAll();
    }
    
    @PreDestroy
    void dispose(EventBroker broker) {
        broker.unsubscribe(this);
    }

}
