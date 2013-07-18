package cz.muni.fi.cdii.plugin.common.model;

import java.util.Set;

public abstract class Bean implements LabelText {
	public abstract Class getClazz();
	public abstract BeanProperties getBeanProperties();
	public abstract Set<BeanField> getInjectedFields();
	public abstract Set<BeanFunction> getInitializerFunctions();
}
