package cz.muni.fi.cdii.plugin.common.model;

import java.util.Map;

public abstract class BeanFunction {
	public abstract Function getFunction();
	public abstract Map<String, InjectionPointProperties> getInjectionPointProperties();
}
