package cz.muni.fi.cdii.common.model;

import java.util.Set;

/**
 * Anything one can inject in.
 * Fields, methods arguments, constructors arguments.
 */
public interface InjectableLocation {

	public Class getType();
	public Set<Class> getEffectiveTypeSet();
}
