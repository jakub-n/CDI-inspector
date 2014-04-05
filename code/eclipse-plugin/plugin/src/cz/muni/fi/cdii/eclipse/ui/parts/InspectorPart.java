package cz.muni.fi.cdii.eclipse.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import cz.muni.fi.cdii.common.model.Bean;
import cz.muni.fi.cdii.eclipse.CdiiEventTopics;
import cz.muni.fi.cdii.eclipse.graph.model.GraphBean;
import cz.muni.fi.cdii.eclipse.inspection.GraphInspection;
import cz.muni.fi.cdii.eclipse.model.LocalBean;
import cz.muni.fi.cdii.eclipse.ui.graph.CdiiGraphViewer;
import cz.muni.fi.cdii.eclipse.ui.graph.ColorManager;
import cz.muni.fi.cdii.eclipse.ui.graph.GraphContentProvider;
import cz.muni.fi.cdii.eclipse.ui.graph.GraphLabelProvider;

@SuppressWarnings("restriction")
public class InspectorPart {

    public static final String ID = "cz.muni.fi.cdii.plugin.InspectorPartDescriptor";

    // private static final String OPEN_FIRST_TIME = "open-first-time";

    @Inject
    private Logger log;

    @Inject
    private IEventBroker broker;

    private Label inspectionPartLabel;
    private CdiiGraphViewer graphViewer;
    private ColorManager colorManager;
    private Composite parent;

    private GraphInspection inspection;

    public InspectorPart() {
        System.out.println("Inspector part init()");
    }

    /**
     * Create contents of the view part.
     */
    @PostConstruct
	public void createControls(Composite parent 
	        ,MPart mPart, 
	        @Preference(nodePath="cz.muni.fi.cdii.eclipse") IEclipsePreferences preferences,
	        MApplication application,
	        EModelService modelService,
	        EPartService partService
	        ) {
	    //setMPartPosition(mPart, preferences, modelService, partService, application);
	    this.parent = parent;
		this.colorManager = new ColorManager();
		parent.setLayout(new GridLayout(1, true));
		
		this.inspectionPartLabel = new Label(parent, SWT.NONE);
		this.inspectionPartLabel.setText("Inspector part");
		if (this.log != null) {
			log.info("log injected into inspection part");
		}
		this.graphViewer = new CdiiGraphViewer(parent, SWT.BORDER);
		this.graphViewer.getGraphControl().setLayoutData(
		        new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.graphViewer.setContentProvider(new GraphContentProvider());
		this.graphViewer.setLabelProvider(
		        new GraphLabelProvider(this.colorManager, this.graphViewer));
		CompositeLayoutAlgorithm layoutAlgorithm = new CompositeLayoutAlgorithm(
		        new LayoutAlgorithm[] { 
		                new TreeLayoutAlgorithm() /*new SpringLayoutAlgorithm()*/, 
		                new DirectedGraphLayoutAlgorithm(DirectedGraphLayoutAlgorithm.VERTICAL) });
		this.graphViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm());
		addGraphContextMenu();
		
		// TODO delete selection listener
		this.graphViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                System.out.println("new graph selection: " + event.getSelection() + " | " 
                        + event.getSelection().getClass());
            }
        });
	}

    private void addGraphContextMenu() {
        MenuManager menuManager = new MenuManager("#PopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
            
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                
                IStructuredSelection graphSelection = (IStructuredSelection) InspectorPart.this
                        .graphViewer.getSelection();
                if (graphSelection.size() == 1) {
                    final LocalBean localBean = getLocalBeanFromSelection(graphSelection);
                    if (localBean == null) {
                        return;
                    }
                    String actionName = "Open in editor";
                    Action action = new Action(actionName) {
                        public void run() {
                            localBean.open();
                        }
                    };
                    manager.add(action);
                }
            }

            private LocalBean getLocalBeanFromSelection(IStructuredSelection selection) {
                Object element = selection.getFirstElement();
                if (element instanceof GraphBean) {
                    GraphBean graphBean = (GraphBean) element;
                    return getLocalBean(graphBean);
                }
                return null;
            }

            private LocalBean getLocalBean(GraphBean graphBean) {
                Bean bean = graphBean.getOrigin();
                if (bean instanceof LocalBean) {
                    return (LocalBean) bean;
                }
                return null;
            }
        });
		Menu contextMenu = menuManager.createContextMenu(this.graphViewer.getControl());
		this.graphViewer.getControl().setMenu(contextMenu);
    }

    // private void setMPartPosition(MPart mPart, IEclipsePreferences preferences,
    // EModelService modelService, EPartService partService, MApplication application) {
    // boolean isOpenedForTheFirstTime = preferences.getBoolean(OPEN_FIRST_TIME, true);
    // if (isOpenedForTheFirstTime) {
    // setFirstOpenFlag(preferences);
    // showPartInPrimaryStack(mPart, modelService, partService, application);
    // }
    //
    // }

    // private static void showPartInPrimaryStack(MPart mPart, EModelService modelService,
    // EPartService partService, MApplication application) {
    // final String primaryPartStackId = "org.eclipse.e4.primaryDataStack";
    // MPartStack partStack = (MPartStack) modelService.find(primaryPartStackId, application);
    // if (partStack != null) {
    // partStack.getChildren().add(mPart);
    // partService.showPart(mPart, PartState.ACTIVATE);
    // }
    // }

    // private void setFirstOpenFlag(IEclipsePreferences preferences) {
    // preferences.putBoolean(OPEN_FIRST_TIME, false);
    // try {
    // preferences.flush();
    // } catch (BackingStoreException e) {
    // this.log.warn(e, "preferences flush failed");
    // }
    // }

    @PreDestroy
    public void dispose() {
        this.colorManager.dispose();
    }

    @Focus
    public void setFocus() {
        this.graphViewer.getGraphControl().setFocus();
    }

    public void inspect(GraphInspection inspection) {
        this.inspection = inspection;
        this.updateFilterWindow();
        this.updateGraph();
    }

    private void updateFilterWindow() {
        broker.post(CdiiEventTopics.UPDATE_FILTER_LABELS, this.inspection);
    }

    /**
     * Rerun inspection. It collects and draw new data.
     */
    public void reinspect() {
        if (this.inspection != null) {
            this.inspection.getTask().run();
        }
    }

    public void zoomIn() {
        this.graphViewer.zoomIn();
    }

    public void zoomOut() {
        this.graphViewer.zoomOut();
    }

    public void resetZoom() {
        this.graphViewer.resetZoom();
    }

    public void relayout() {
        this.graphViewer.applyLayout();
    }
    
    public void expandAllNodes() {
        this.graphViewer.openAllContainer();
    }
    
    public void collapseAllNode() {
        this.graphViewer.closeAllContainer();
    }

    private void updateGraph() {
        this.graphViewer.setInput(this.inspection.getFramedGraph());
        this.graphViewer.applyLayout();
        this.setFocus();
        this.parent.redraw();
        this.parent.update();
    }

}
