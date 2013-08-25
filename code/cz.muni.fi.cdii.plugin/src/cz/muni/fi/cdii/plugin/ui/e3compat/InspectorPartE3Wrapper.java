package cz.muni.fi.cdii.plugin.ui.e3compat;

import org.eclipse.e4.tools.compat.parts.DIViewPart;

import cz.muni.fi.cdii.plugin.ui.InspectorPart;

/**
 * Encapsulation of standard e4 POJO part model class. It allows to utilize standard eclipse 3.x 
 * features like:
 * <ul>
 * <li>Show View dialog (shift-alt-q q)</>
 * <li>Outline view</li>
 * <li>Properties view</li>
 * </ul>
 *
 */
public class InspectorPartE3Wrapper extends DIViewPart<InspectorPart> {
	
	/**
	 * View ID as defined in {@code plugin.xml} descriptor
	 */
	public static String VIEW_ID = "cz.muni.fi.cdii.plugin.cdiInspectorView";

	public InspectorPartE3Wrapper() {
		super(InspectorPart.class);
	}

	// TODO
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return super.getAdapter(adapter);
	}
	
	

}
