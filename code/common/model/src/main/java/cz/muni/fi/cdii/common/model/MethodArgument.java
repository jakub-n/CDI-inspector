package cz.muni.fi.cdii.common.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class MethodArgument implements InjectableLocation {

	@JsonProperty
	private Class type;

	@JsonProperty
	private Method parent;
	
	public Class getType() {
		// TODO Auto-generated method stub
		return null;
	}
	public Set<Class> getEffectiveTypeSet() {
		// TODO Auto-generated method stub
		return null;
	}
}
