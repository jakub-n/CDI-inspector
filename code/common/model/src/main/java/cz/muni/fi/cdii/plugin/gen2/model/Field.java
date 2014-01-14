package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

public class Field implements InjectableLocation {
	
	private Class parent;
	private Class type;
	/**
	 * null if field is not annotated by {@link @Produces}
	 */
	private Bean produces;
	
	public Class getType() {
		// TODO Auto-generated method stub
		return null;
	}
	public Set<Class> getEffectiveTypeSet() {
		// TODO Auto-generated method stub
		return null;
	}
}
