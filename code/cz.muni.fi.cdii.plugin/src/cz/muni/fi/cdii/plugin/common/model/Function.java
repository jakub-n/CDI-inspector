package cz.muni.fi.cdii.plugin.common.model;

import java.util.List;

import cz.muni.fi.cdii.plugin.common.model.internal.ClassMember;

public abstract class Function extends ClassMember {
	public abstract List<String> getArgumentTypeNames();
}
