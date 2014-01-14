package cz.muni.fi.cdii.plugin.common.model;

public abstract class BeanField implements LabelText {
	public abstract Field getField();
	public abstract InjectionPointProperties getInjectionPointProperties();
	
	@Override
	public String toLabelString() {
		StringBuilder builder = new StringBuilder();
		builder.append("@Inject\n");
		builder.append(this.getInjectionPointProperties().toLabelString());
		builder.append(getField().toLabelString());
		String result = builder.toString();
		return result;
	}
	
}
