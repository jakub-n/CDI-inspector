package cz.muni.fi.cdii.plugin.common.model;

import java.util.Map;

public abstract class BeanFunction implements LabelText {
	public abstract Function getFunction();

	public abstract Map<String, InjectionPointProperties> getInjectionPointProperties();

	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("@Inject\n");
		// TODO refactor - presunout vypis do tridy Function
		builder.append(this.getFunction().getTypeName()).append(" ");
		builder.append(this.getFunction().getName()).append("(");
		for (Map.Entry<String, InjectionPointProperties> entry : this.getInjectionPointProperties().
				entrySet()) {
			if (entry.getValue().getScopeDeclaration() != null) {
				builder.append('@').append(entry.getValue().getScopeDeclaration()).append(" ");
			}
			for (String qualifier : entry.getValue().getQualifiers()) {
				builder.append("@").append(qualifier).append(" ");
			}
			for (String stereotype : entry.getValue().getStereotypes()) {
				builder.append("@").append(stereotype).append(" ");
			}
			builder.append(entry.getKey()).append(", ");
		}
		if (! this.getInjectionPointProperties().isEmpty()) {
			final int length = builder.length();
			builder.delete(length - 2, length);
		}
		builder.append(")");
		
		String result = builder.toString();
		return result;
	}
}
