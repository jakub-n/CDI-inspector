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
		outputText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		if (this.log != null) {
			log.info("log injected into inspection part");
		}
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
	}

}
