package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

/**
 * Model class representing class node in the graph.
 *
 */
public abstract class Class implements LabelText {
	
	public abstract String getPackageName();
	public abstract String getClassName();
	public abstract boolean openable();
	public abstract void open();
	public abstract Set<Field> getFields();
	public abstract Set<Function> getFunctions();
	public abstract Set<? extends Bean> getBeans();
	
	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getPackageName()).append("\n");
		builder.append(this.getClassName());
		String result = builder.toString();
		return result;
	}
	@Override
	public String toString() {
		return this.getPackageName() + " " + this.getClassName();
	}
}
