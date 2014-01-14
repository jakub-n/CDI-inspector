package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.Set;

/**
 * Anything one can inject in.
 * Fields, methods arguments, constructors arguments.
 */
public interface InjectableLocation {

	public Class getType();
	public Set<Class> getEffectiveTypeSet();
}
