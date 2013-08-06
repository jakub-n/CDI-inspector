package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

/**
 * Root class of graph model.
 *
 */
public abstract class CdiInspection {
	
	public static final String INSPECT_TOPIC = "cz-muni-fi-cdii-plugin-common-inspect";
	
	public abstract Set<? extends Class> getClasses();
	
	/**
	 * @return Up to date model of inspected project.
	 */
	public abstract CdiInspection refresh();
}
