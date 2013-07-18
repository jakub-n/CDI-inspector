package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

// TODO javadoc
public abstract class Class implements LabelText {
	
	public abstract String getPackageName();
	public abstract String getClassName();
	public abstract boolean openable();
	public abstract void open();
	public abstract Set<Field> getFields();
	public abstract Set<Function> getFunctions();
}
