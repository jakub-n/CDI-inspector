package cz.muni.fi.cdii.plugin.gen2.model;

import java.util.List;

/**
 * Represents java type. For non-parametrized types it is equal to {@link Class}. For parametrized 
 * types there can be multiple {@link Type}s for one {@link Class}.
 *
 */
public class Type {
	
	/**
	 * null iff {@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
	 */
	private Class clazz;
	/**
	 * null if 
	 * <ul>
	 * <li>{@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
	 * <li>type is non-parametrized
	 * </ul>
	 */
	private List<Type> typeParameters;
	private TypeWildcardEnum wildcardType;
}
