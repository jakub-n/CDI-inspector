package cz.muni.fi.cdii.common.model;

import java.util.List;
import java.util.Map;

public class AnnotationClass {

	private String name;
	private List<String> packageElements; // TODO maybe replace
	
	/**
	 * name => type
	 */
	private Map<String, java.lang.Class<?>> params;
	
}
