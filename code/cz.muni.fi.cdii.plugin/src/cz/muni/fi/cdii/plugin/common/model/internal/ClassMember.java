package cz.muni.fi.cdii.plugin.common.model.internal;

import cz.muni.fi.cdii.plugin.common.model.Bean;
import cz.muni.fi.cdii.plugin.common.model.LabelText;


public abstract class ClassMember implements LabelText {
	public abstract String getName();
	public abstract String getTypeName();
	public abstract Bean getProducedBean();
}
