package cz.muni.fi.cdii.plugin.gen2.model;

/**
 * Enum specifies kind of type wildcard.
 * 
 * @see Type
 */
public enum TypeWildcardEnum {
	/**
	 * no wildcard
	 */
	NONE,
	/**
	 * used for e.g. {@code Set<?>}
	 */
	QUESTIONMARK,
	/**
	 * used for e.g. {@code Set<? extends String>}
	 */
	EXTENDS,
	/**
	 * used for e.g. {@code Set<? super String>}
	 */
	SUPER
}
