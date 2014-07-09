package cz.muni.fi.cdii.poc.jsonserialization.model.manyrefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class ObjC {
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjC) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}
}
