package cz.muni.fi.cdii.plugin.common.model;

import cz.muni.fi.cdii.plugin.common.model.internal.CdiProperties;

public abstract class InjectionPointProperties extends CdiProperties implements LabelText {

	/**
	 * Can return null if there is no explicit scope declaration
	 */
	public abstract ScopeDeclarationEnum getScopeDeclaration();

	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		if (this.getScopeDeclaration() != null) {
			builder.append(this.getScopeDeclaration()).append("\n");
		}
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
