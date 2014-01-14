package cz.muni.fi.cdii.plugin.common.model;

import cz.muni.fi.cdii.plugin.common.model.internal.ClassMember;

public abstract class Field extends ClassMember {

	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getTypeName()).append(" ");
		builder.append(this.getName());
		String result = builder.toString();
		return result;
	}
}
