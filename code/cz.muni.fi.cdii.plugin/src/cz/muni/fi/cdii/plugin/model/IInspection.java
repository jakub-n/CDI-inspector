package cz.muni.fi.cdii.plugin.model;

import java.util.Collection;

public interface IInspection {
	
	public static final String INSPECT_TOPIC = "cz-muni-fi-cdii-plugin-inspect";

	public Collection<IBean> getBeans();
}
