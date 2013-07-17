package cz.muni.fi.cdii.plugin.visual.model;

import java.util.List;

import cz.muni.fi.cdii.plugin.visual.model.internal.ClassMember;

public abstract class Function extends ClassMember {
	public abstract List<String> getArgumentTypeNames();
}
