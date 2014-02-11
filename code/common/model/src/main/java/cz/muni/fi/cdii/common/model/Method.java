package cz.muni.fi.cdii.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Method {


	@JsonProperty
	private Class parent;

	@JsonProperty
	private Class returnType;
	/**
	 * never null
	 */

	@JsonProperty
	private List<MethodArgument> arguments;
	
	/**
	 * null if method is not annotated by {@link @Produces}
	 */

	@JsonProperty
	private Bean produces;
}
