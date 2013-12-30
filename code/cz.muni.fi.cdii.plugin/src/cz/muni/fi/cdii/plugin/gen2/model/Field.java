package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

public class Field implements InjectableLocation {
	
	private Class parent;
	private Class type;
	/**
	 * null if field is not annotated by {@link @Produces}
	 */
	private Bean produces;
	
	@Override
	public Class getType() {
		return this.type;
	}
	
	@Override
	public Set<Class> getEffectiveTypeSet() {
		return this.type.getEffectiveTypeSet();
	}
}
