package cz.muni.fi.cdii.plugin.common.model;

import cz.muni.fi.cdii.plugin.common.model.internal.CdiProperties;

public abstract class InjectionPointProperties extends CdiProperties {
	public abstract ScopeDeclarationEnum getScopeDeclaration();
}
