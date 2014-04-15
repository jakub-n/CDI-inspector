package cz.muni.fi.cdii.eclipse.ui.parts;


import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import cz.muni.fi.cdii.eclipse.CdiiEventTopics;
import cz.muni.fi.cdii.eclipse.inspection.GraphInspection;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.CategoryFilterModel;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.CheckboxColumnLabelProvider;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterContentProvider;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterModel;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.FilterPartModelFactory;
import cz.muni.fi.cdii.eclipse.ui.parts.filter.LabelColumnLabelProvider;

import org.eclipse.swt.widgets.TreeColumn;

public class FilterPart implements EventHandler {
    
    public static final String PART_ID = "cz.muni.fi.cdii.eclipse.plugin.partdescriptor.filter";
    
    private Text text;
    private TreeViewer treeViewer;
    private CheckboxColumnLabelProvider checkboxColumnLabelProvider;
    
    @Inject
    private EventBroker broker;

    private TreeViewerColumn checkboxColumn;

    private TreeViewerColumn labelColumn;
    
    @PostConstruct
    public void createControls(Composite parent) {
        this.broker.subscribe(CdiiEventTopics.UPDATE_FILTER_LABELS, this);
        parent.setLayout(new GridLayout(2, false));
        
        Label classNameLabel = new Label(parent, SWT.NONE);
        classNameLabel.setText("Type name:");
        
        this.text = new Text(parent, SWT.BORDER);
        this.text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.text.addModifyListener(createTextModifyListener());
        
        this.treeViewer = new TreeViewer(parent, SWT.BORDER);
        Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        this.treeViewer.setContentProvider(new FilterContentProvider());
        this.labelColumn = new TreeViewerColumn(this.treeViewer, SWT.NONE);
        this.labelColumn.setLabelProvider(new LabelColumnLabelProvider());
        this.labelColumn.getColumn().setText("Criterion");
        this.labelColumn.getColumn().setResizable(true);
        this.checkboxColumn = new TreeViewerColumn(this.treeViewer, SWT.NONE);
        TreeColumn treeColumn_1 = this.checkboxColumn.getColumn();
        treeColumn_1.setAlignment(SWT.CENTER);
        this.checkboxColumnLabelProvider = new CheckboxColumnLabelProvider(this);
        this.checkboxColumn.setLabelProvider(checkboxColumnLabelProvider);
        this.checkboxColumn.getColumn().setText("");
        this.checkboxColumn.getColumn().setResizable(false);
        this.treeViewer.getTree().addControlListener(createResizeListener());
        
        Button resetFilterButton = new Button(parent, SWT.NONE);
        resetFilterButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        resetFilterButton.setText("Reset filter");
        resetFilterButton.addSelectionListener(createResetButtonListener());
        this.broker.post(CdiiEventTopics.UPDATE_FILTER_LABELS_REQUEST, null);
    }

    private ControlListener createResizeListener() {
        return new ControlListener(){

            @Override
            public void controlMoved(ControlEvent e) {
            }

            @Override
            public void controlResized(ControlEvent e) {
                FilterPart.this.labelColumn.getColumn().setWidth(
                        FilterPart.this.treeViewer.getTree().getSize().x - 40);
                FilterPart.this.checkboxColumn.getColumn().setWidth(20);
            }
        };
    }

    private ModifyListener createTextModifyListener() {
        return new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                FilterPart.this.doFilter();
            }
            
        };
    }

    private SelectionListener createResetButtonListener() {
        return new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                FilterPart.this.reset();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        };
    }

    /**
     * This should be called after some input in this part is changed.
     * Method collect filtering data and send filter event
     */
    public void doFilter() {
        FilterModel filterCriteria = new FilterModel();
        filterCriteria.setClassName(this.text.getText().trim());
        Set<String> packageCriterion = getCriterion(FilterPartModelFactory.CATEGORY_PACKAGES);
        filterCriteria.setPackages(packageCriterion);
        Set<String> elNameCriterion = getCriterion(FilterPartModelFactory.CATEGORY_EL_NAMES);
        filterCriteria.setElNames(elNameCriterion);
        Set<String> typesCriterion = getCriterion(FilterPartModelFactory.CATEGORY_TYPES);
        filterCriteria.setTypes(typesCriterion);
        Set<String> qualifiersCriterion = getCriterion(FilterPartModelFactory.CATEGORY_QUALIFIERS);
        filterCriteria.setQualifiers(qualifiersCriterion);
        this.broker.post(CdiiEventTopics.FILTER_GRAPH, filterCriteria);
    }
    
    private Set<String> getCriterion(String categoryName) {
        CategoryFilterModel category = getCategoryByName(categoryName);
        Set<String> filterSet = this.checkboxColumnLabelProvider.getFilterSetForCategory(category);
        return filterSet;
    }

    private CategoryFilterModel getCategoryByName(String categoryName) {
        CategoryFilterModel[] categories = (CategoryFilterModel[]) this.treeViewer.getInput();
        for (CategoryFilterModel currentCategory : categories) {
            if (categoryName.equals(currentCategory.getName())) {
                return currentCategory;
            }
        }
        throw new RuntimeException("Category of given name not found: '" + categoryName + "'");
    }

    @Focus
    public void setFocus() {
        this.text.setFocus();
    }
    
    @PreDestroy
    public void dispose() {
        this.broker.unsubscribe(this);
    }

    /**
     * {@link CdiiEventTopics#UPDATE_FILTER_LABELS}
     */
    @Override
    public void handleEvent(Event event) {
        String topic = event.getTopic();
        if (CdiiEventTopics.UPDATE_FILTER_LABELS.equals(topic)) {
            this.checkboxColumnLabelProvider.disposeWidgets();
            final GraphInspection inspection = 
                    (GraphInspection) event.getProperty(IEventBroker.DATA);
            CategoryFilterModel[] providerInput = FilterPartModelFactory.create(inspection);
            this.treeViewer.setInput(providerInput);
            this.treeViewer.expandAll();
            this.text.setText("");
            return;
        }
    }

    private void reset() {
        this.text.setText("");
        this.checkboxColumnLabelProvider.reset();
        this.text.setFocus();
        this.doFilter();
    }
}
