package cz.muni.fi.cdii.common.model;

import java.util.List;

public class Method {

	private Class parent;
	private Class returnType;
	/**
	 * never null
	 */
	private List<MethodArgument> arguments;
	/**
	 * null if method is not annotated by {@link @Produces}
	 */
	private Bean produces;
}