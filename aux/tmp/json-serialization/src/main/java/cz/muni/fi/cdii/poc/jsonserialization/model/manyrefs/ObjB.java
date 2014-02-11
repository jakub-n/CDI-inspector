package cz.muni.fi.cdii.poc.jsonserialization.model.manyrefs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown=true)
public class ObjB {


	@JsonProperty
	public Obj objA;
	
	@JsonProperty
	public ObjC objC;
	
	@Override
	public String toString() {
		return "ObjB " + this.hashCode() + " [objA=" + ManyRefsMain.getHashCode(objA) 
				+ ", objC=" + ManyRefsMain.getHashCode(objC) + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjB) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode();
	}
}
