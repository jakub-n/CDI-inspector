package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

public abstract class Bean implements LabelText {
	
	public abstract Class getClazz();
	public abstract CdiProperties getBeanProperties();
	public abstract Set<BeanField> getInjectedFields();
	public abstract Set<BeanFunction> getInitializerFunctions();
	
	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getBeanProperties().toLabelString());
		builder.append("bean ").append(this.getClazz().getClassName());
		String result = builder.toString();
		return result;
	}
}
