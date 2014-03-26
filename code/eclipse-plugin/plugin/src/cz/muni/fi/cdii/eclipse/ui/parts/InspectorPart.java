package cz.muni.fi.cdii.eclipse.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;
import cz.muni.fi.cdii.plugin.ui.ColorManager;
import cz.muni.fi.cdii.plugin.ui.GraphContentProvider;
import cz.muni.fi.cdii.plugin.ui.GraphLabelProvider;

@SuppressWarnings("restriction")
public class InspectorPart {
	
	public static final String ID = "cz.muni.fi.cdii.plugin.InspectorPartDescriptor";
	
	@Inject
	private Logger log;

	private Label inspectionPartLabel;
	private GraphViewer graphViewer;
	private ColorManager colorManager;

	public InspectorPart() {
		System.out.println("Inspector part init()");
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
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
	}

}
