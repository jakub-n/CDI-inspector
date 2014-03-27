package cz.muni.fi.cdii.eclipse.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.osgi.service.prefs.BackingStoreException;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;
import cz.muni.fi.cdii.plugin.ui.ColorManager;
import cz.muni.fi.cdii.plugin.ui.GraphContentProvider;
import cz.muni.fi.cdii.plugin.ui.GraphLabelProvider;

@SuppressWarnings("restriction")
public class InspectorPart {
	
	public static final String ID = "cz.muni.fi.cdii.plugin.InspectorPartDescriptor";

    private static final String OPEN_FIRST_TIME = "open-first-time";
	
	@Inject
	private Logger log;

	private Label inspectionPartLabel;
	private GraphViewer graphViewer;
	private ColorManager colorManager;
	private Composite parent;

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
	    setMPartPosition(mPart, preferences, modelService, partService, application);
	    this.parent = parent;
		this.colorManager = new ColorManager();
		parent.setLayout(new GridLayout(1, true));
		
		this.inspectionPartLabel = new Label(parent, SWT.NONE);
		this.inspectionPartLabel.setText("Inspector part");
		if (this.log != null) {
			log.info("log injected into inspection part");
		}
		this.graphViewer = new GraphViewer(parent, SWT.BORDER);
		this.graphViewer.getGraphControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.graphViewer.setContentProvider(new GraphContentProvider());
		this.graphViewer.setLabelProvider(new GraphLabelProvider(this.colorManager));
		this.graphViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));

		
	}

	private void setMPartPosition(MPart mPart, IEclipsePreferences preferences, 
	        EModelService modelService, EPartService partService, MApplication application) {
	    boolean isOpenedForTheFirstTime = preferences.getBoolean(OPEN_FIRST_TIME, true);
        if (isOpenedForTheFirstTime) {
            setFirstOpenFlag(preferences);
            showPartInPrimaryStack(mPart, modelService, partService, application);
        }
        
    }

    private static void showPartInPrimaryStack(MPart mPart, EModelService modelService,
            EPartService partService, MApplication application) {
        final String primaryPartStackId = "org.eclipse.e4.primaryDataStack";
        MPartStack partStack = (MPartStack) modelService.find(primaryPartStackId, application);
        if (partStack != null) {
            partStack.getChildren().add(mPart);
            partService.showPart(mPart, PartState.ACTIVATE);
        }
    }

    private void setFirstOpenFlag(IEclipsePreferences preferences) {
        preferences.putBoolean(OPEN_FIRST_TIME, false);
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            this.log.warn(e, "preferences flush failed");
        }
    }

    @PreDestroy
	public void dispose() {
		this.colorManager.dispose();
	}

	@Focus
	public void setFocus() {
		this.graphViewer.getGraphControl().setFocus();
	}
	
	public void inspect(CdiInspection inspection) {
		log.info("inspectionPart.inspect() called");
		
		cz.muni.fi.cdii.plugin.common.model.Class[] classes = inspection.getClasses().toArray(
				new cz.muni.fi.cdii.plugin.common.model.Class[0]);
		this.graphViewer.setInput(classes);
		this.graphViewer.applyLayout();
		this.setFocus();
		this.parent.redraw();
		this.parent.update();
	}

}
