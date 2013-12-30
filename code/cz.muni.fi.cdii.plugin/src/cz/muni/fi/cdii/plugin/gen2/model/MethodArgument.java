package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

public class MethodArgument implements InjectableLocation {
	
	private Class type;
	private Method parent;

	@Override
	public Class getType() {
		return this.getType();
	}

	@Override
	public Set<Class> getEffectiveTypeSet() {
		return this.type.getEffectiveTypeSet();
	}
	
}
