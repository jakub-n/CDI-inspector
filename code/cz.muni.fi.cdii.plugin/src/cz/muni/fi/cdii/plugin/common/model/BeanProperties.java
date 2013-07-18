package cz.muni.fi.cdii.plugin.common.model;

import cz.muni.fi.cdii.plugin.common.model.internal.CdiProperties;

public abstract class BeanProperties extends CdiProperties {
	public abstract EffectiveScopeEnum getEffectiveScope();
}
