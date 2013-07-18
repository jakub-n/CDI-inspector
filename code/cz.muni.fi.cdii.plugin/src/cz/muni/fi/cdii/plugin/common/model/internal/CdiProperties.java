package cz.muni.fi.cdii.plugin.common.model.internal;

import java.util.Set;

public abstract class CdiProperties {
	public abstract String getName();
	//  TODO smazat, pokud se nepouzije; pripadne presunout do potomka
	public abstract String getTypeName();
	public abstract Set<String> getQualifiers();
	public abstract Set<String> getStereotypes();
}
