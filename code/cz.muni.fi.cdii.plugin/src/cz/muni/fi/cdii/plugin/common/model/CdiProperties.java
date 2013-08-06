package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

public abstract class CdiProperties implements LabelText {
	public abstract Scope getScope();
	public abstract String getName();
	//  TODO smazat, pokud se nepouzije; pripadne presunout do potomka
	public abstract String getTypeName();
	public abstract Set<String> getQualifiers();
	public abstract Set<String> getStereotypes();
	
	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getScope()).append("\n");
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
