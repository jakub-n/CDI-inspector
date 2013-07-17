package cz.muni.fi.cdii.plugin.visual.model;

import cz.muni.fi.cdii.plugin.visual.model.internal.CdiProperties;

public abstract class InjectionPointProperties extends CdiProperties {
	public abstract ScopeDeclarationEnum getScopeDeclaration();
}
