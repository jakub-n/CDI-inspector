package cz.muni.fi.cdii.plugin.visual.model;

import cz.muni.fi.cdii.plugin.visual.model.internal.CdiProperties;

public abstract class BeanProperties extends CdiProperties {
	public abstract EffectiveScopeEnum getEffectiveScope();
}
