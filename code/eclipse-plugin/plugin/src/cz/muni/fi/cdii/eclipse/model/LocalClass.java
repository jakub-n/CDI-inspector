package cz.muni.fi.cdii.eclipse.model;

import cz.muni.fi.cdii.common.model.Class;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=LocalClass.class)
public class LocalClass extends Class {

	public LocalClass(String name) {
		super(name);
	}
	


}
