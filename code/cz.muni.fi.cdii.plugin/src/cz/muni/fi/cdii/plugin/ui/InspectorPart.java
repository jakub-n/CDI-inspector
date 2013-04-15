package cz.muni.fi.cdii.plugin.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

import cz.muni.fi.cdii.plugin.model.IInspection;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class InspectorPart {
	
	public static final String ID = "cz.muni.fi.cdii.plugin.InspectorPartDescriptor";
	
	@Inject
	Logger log;

	private Label inspectionPartLabel;
	private Text outputText;

	private Graph graph;

	public InspectorPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		
		this.inspectionPartLabel = new Label(parent, SWT.NONE);
		inspectionPartLabel.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.BOLD));
		this.inspectionPartLabel.setText("Inspector part");
		
		outputText = new Text(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		outputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		if (this.log != null) {
			log.info("log injected into inspection part");
		}
		this.graph = new Graph(parent, SWT.BORDER);
		graph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		this.inspectionPartLabel.setFocus();
	}
	
	public void inspect(IInspection inspection) {
		log.info("inspectionPart.inspect() called");
		this.outputText.setText(inspection.toString());
		GraphNode node1 = new GraphNode(this.graph, SWT.NONE, "ahoj");
		GraphNode node2 = new GraphNode(this.graph, SWT.NONE, "svete");
		GraphConnection graphConnection = new GraphConnection(this.graph, SWT.NONE,
		        node1, node2);
		this.graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
	}

}
