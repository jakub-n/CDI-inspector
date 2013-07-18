package cz.muni.fi.cdii.plugin.common.model;

import cz.muni.fi.cdii.plugin.common.model.internal.CdiProperties;

public abstract class BeanProperties extends CdiProperties implements LabelText {
	public abstract EffectiveScopeEnum getEffectiveScope();

	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getEffectiveScope()).append("\n");
		for (String qualifier : this.getQualifiers()) {
			builder.append("@").append(qualifier).append("\n");
		}
		for (String stereotype : this.getStereotypes()) {
			builder.append("@").append(stereotype).append("\n");
		}
		String result = builder.toString();
		return result;
	}
}
