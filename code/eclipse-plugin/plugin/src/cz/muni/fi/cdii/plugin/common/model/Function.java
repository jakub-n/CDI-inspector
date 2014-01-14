package cz.muni.fi.cdii.plugin.common.model;

import java.util.List;

import cz.muni.fi.cdii.plugin.common.model.internal.ClassMember;

public abstract class Function extends ClassMember {
	public abstract List<String> getArgumentTypeNames();

	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getTypeName()).append(" ");
		builder.append(this.getName()).append("(");
		for (String argumentType : this.getArgumentTypeNames()) {
			builder.append(argumentType).append(", ");
		}
		if (! this.getArgumentTypeNames().isEmpty()) {
			final int length = builder.length();
			builder.delete(length - 2, length);
		}
		builder.append(")");
		String result = builder.toString();
		return result;
	}
}
