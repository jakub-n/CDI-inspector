package cz.muni.fi.cdii.plugin.visual.model.internal;

import cz.muni.fi.cdii.plugin.visual.model.Bean;
import cz.muni.fi.cdii.plugin.visual.model.LabelText;


public abstract class ClassMember implements LabelText {
	public abstract String getName();
	public abstract String getTypeName();
	public abstract Bean getProducedBean();
}
